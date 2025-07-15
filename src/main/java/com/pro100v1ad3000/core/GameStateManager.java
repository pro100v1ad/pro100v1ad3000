package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.entities.players.LocalPlayer;
import main.java.com.pro100v1ad3000.entities.players.Player;
import main.java.com.pro100v1ad3000.entities.players.RemotePlayer;
import main.java.com.pro100v1ad3000.network.client.NetworkClient;
import main.java.com.pro100v1ad3000.network.packets.*;
import main.java.com.pro100v1ad3000.network.server.NetworkServer;
import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.utils.Config;
import main.java.com.pro100v1ad3000.utils.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GameStateManager {

    private final Map<Integer, Player> players = new ConcurrentHashMap<>();
    private LocalPlayer localPlayer;
    private NetworkClient networkClient;
    private NetworkServer networkServer;
    private boolean isMultiplayer = false;
    private boolean isHost = false;
    private String serverAddress;

    private static final int MAX_RECONNECT_ATTEMPTS = 8;
    private static final int RECONNECT_DELAY_MS = 5000;

    private final InputManager inputManager;

    public GameStateManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public void initSinglePlayer() { // Инициирует одиночную игру
        // Инициализирует игру в режиме одного игрока
        isMultiplayer = false;
        players.clear();
        localPlayer = new LocalPlayer(1, 0, 0, null);
        players.put(localPlayer.getId(), localPlayer);

        // Создаем одиночную игру
    }

    public void initMultiplayer(boolean isHost, String hostAddress) { // Инициирует многопользовательскую игру
        // Инициализирует многопользовательскую игру, устанавливая роль хоста и адрес сервера
        this.isHost = isHost;
        this.serverAddress = hostAddress;
        this.isMultiplayer = true;
        players.clear();

        if (isHost) {
            startServer(); // Запускает сервер, если текущий экземпляр является хостом
        }

        startClient(); // Подключает клиент к серверу
    }

    private void startServer() { // Запускает сервер
        // Создает и запускает сервер на порту 12345 с обработчиком входящих пакетов
        networkServer = new NetworkServer(Config.SERVER_PORT, this::handleServerPacket);
        networkServer.start();
        Logger.info("Server started");
    }

    private void startClient() { // Создает клиент
        // Создает и подключает клиент к серверу с заданными параметрами подключения
        networkClient = new NetworkClient(
                isHost ? "localhost" : serverAddress,
                12345,
                MAX_RECONNECT_ATTEMPTS,
                RECONNECT_DELAY_MS,
                this::handleClientPacket,
                this::onConnectionLost
        );

        if(networkClient.connect()) {
            int playerId = generatePlayerId();
            localPlayer = new LocalPlayer(playerId, 0, 0, networkClient);
            players.put(localPlayer.getId(), localPlayer);

            networkClient.sendPacket(new PlayerConnectPacket(playerId, 0, 0));
            Logger.info("Player connected to server id: " + playerId);

        } else {
            Logger.error("Failed to connect to server");
        }
    }

    private void handleServerPacket(NetworkServer.ClientHandler client, Object packet) { // Обрабатывает пакет на сервере
        // Обрабатывает входящие пакеты на стороне сервера
        // Логика обработки пакетов от клиентов
        if(packet instanceof PlayerConnectPacket) {
            PlayerConnectPacket connectPacket = (PlayerConnectPacket) packet;
            client.setPlayerId(connectPacket.getPlayerId());

            if(localPlayer != null) {
                updatePlayerPosition(connectPacket.getPlayerId(), connectPacket.getX(), connectPacket.getY());
            }

            networkServer.broadcast(packet, null);

            // Можно сразу отправить список игроков новому клиенту
            sendAllPlayersToClient(client);

        } else if(packet instanceof PlayerMovePacket) {
            PlayerMovePacket movePacket = (PlayerMovePacket) packet;

            if(client.getPlayerId() == null) {
                client.setPlayerId(movePacket.getPlayerId());
            }

            updatePlayerPosition(movePacket.getPlayerId(), movePacket.getX(), movePacket.getX());
            networkServer.broadcast(packet, client);
        } else if (packet instanceof RequestPlayersPacket) {
            // Отправляем новому клиенту список всех игроков
             sendAllPlayersToClient(client);
        }
    }

    private void handleClientPacket(Object packet) { // обрабатывает пакет на клиенте
        // Обрабатывает входящие пакеты на стороне клиента
        // Логика обработки пакетов от сервера

        if(packet instanceof PlayerMovePacket) {
            PlayerMovePacket movePacket = (PlayerMovePacket) packet;
            updatePlayerPosition(movePacket.getPlayerId(), movePacket.getX(), movePacket.getY());
        } else if (packet instanceof ReconnectPacket) {
            ReconnectPacket reconnectPacket = (ReconnectPacket) packet;
            handleReconnect(reconnectPacket);

        } else if (packet instanceof PlayersListPacket) {
            // Получаем полный список игроков при подключении
            PlayersListPacket listPacket = (PlayersListPacket) packet;
            updatePlayersList(listPacket.getPlayers());
        } else if (packet instanceof PlayerDisconnectedPacket) {
            PlayerDisconnectedPacket discPacket = (PlayerDisconnectedPacket) packet;
            players.remove(discPacket.getPlayerId());
        }

    }

    private void handleReconnect(ReconnectPacket packet) {
        Player player = players.get(packet.getPlayerId());
        if(player != null) {
            player.setPosition(packet.getX(), player.getY());
            Logger.info("Player " + packet.getPlayerId() + " reconnected");
        }
    }

    private void sendAllPlayersToClient(NetworkServer.ClientHandler client) {
        List<PlayerData> playersData = players.values().stream()
                .map(p -> new PlayerData(p.getId(), p.getX(), p.getY()))
                .collect(Collectors.toList());
        client.sendPacket(new PlayersListPacket(playersData));
    }

    private void updatePlayersList(List<PlayerData> playersData) {
        // Удаляем отсутствующих игроков
        Set<Integer> currentIds = playersData.stream()
                .map(PlayerData::getId)
                .collect(Collectors.toSet());

        players.keySet().removeIf(id -> !currentIds.contains(id) && id != localPlayer.getId());

        // Добавляем/обновляем игроков
        for(PlayerData data : playersData) {
            if (data.getId() != localPlayer.getId()) {
                updatePlayerPosition(data.getId(), data.getX(), data.getY());
            }
        }

    }

    private void updatePlayerPosition(int playerId, float x, float y) {
        if(localPlayer == null || playerId == localPlayer.getId()) return;

        players.compute(playerId, (id, player) -> {
           if(player == null) {
               return new RemotePlayer(id, x, y);
           } else {
               player.setPosition(x, y);
               return player;
           }
        });
    }

    private void onConnectionLost() { // Потеря соединения с сервером
        // Обрабатывает потерю соединения с сервером и предпринимает попытки переподключения
        if(isHost) {
            Logger.warn("Server stopped");
        } else {
            Logger.warn("Connection to server lost");
        }
        // Уведомить игрока об этом...
    }

    private int generatePlayerId() { // Создает уникальный id
        // Генерирует уникальный идентификатор для игрока
        return new Random().nextInt(1000) + 1;
    }

    private void processInput(float deltaTime) {
        // Обработка клавиатуры
        if(inputManager.isKeyPressed(KeyEvent.VK_W)) {
            localPlayer.move(0, -5);
        }

        if(inputManager.isKeyPressed(KeyEvent.VK_A)) {
            localPlayer.move(-5, 0);
        }

        if(inputManager.isKeyPressed(KeyEvent.VK_S)) {
            localPlayer.move(0, 5);
        }

        if(inputManager.isKeyPressed(KeyEvent.VK_D)) {
            localPlayer.move(5, 0);
        }

        // Обработка мыши
        if(inputManager.isMouseButtonPressed(MouseEvent.BUTTON1)) {
            // Нажата левая кнопка
        }

        int mouseX = inputManager.getMouseX();
        int mouseY = inputManager.getMouseY();

        inputManager.endFrame();
    }

    public void update(float deltaTime) { // тут ВСЯ ЛОГИКА
        // Обновляет состояние игры на основе времени, прошедшего с последнего кадра
        // Логика обновления состояния игры
        processInput(deltaTime);
        players.values().forEach(player -> player.update(deltaTime));

    }

    public void render(Graphics2D g, int currentWidth, int currentHeight) {
        // Очистка экрана
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, currentWidth, currentHeight);

        // рассчитываем масштаб
        float scaleX = currentWidth / (float)Config.BASE_WIDTH;
        float scaleY = currentHeight / (float)Config.BASE_HEIGHT;

        // Сохраняем оригинальные трансформации
        AffineTransform originalTransform = g.getTransform();

        // Применяем масштабирование
        g.scale(scaleX, scaleY);

        // Отрисовка игровых объектов (в координатах 800х600)
        renderGameObjects(g);

        // Восстанавливаем оригинальные трансформации
        g.setTransform(originalTransform);

        // Отрисовка интерфейса
         renderUI(g, currentWidth, currentHeight);

    }

    private void renderGameObjects(Graphics2D g) {
        // Все объекты рисуются как будто экран 800х600
        // Они автоматически растянутся при масштабировании

        g.setColor(Color.PINK);
        g.fillRect(100, 100, 200, 200);

        g.setColor(Color.GREEN);
        g.fillRect(150, 150, 300, 300);

        players.values().forEach(player -> player.draw(g));


    }

    private void renderUI(Graphics2D g, int screenWidth, int screenHeight) {
        // Интерфейс можно рисовать без масштабирования, если нужны точные пиксельные размеры
        if(isMultiplayer && networkClient != null) {
            long ping = networkClient.getLastPing();
            String pingText = ping >= 0 ? "Ping: " + ping + "ms" : "Ping: -";

            g.setColor(Color.WHITE);
            String text = (isHost) ? "Server" : "Client";
            g.drawString(text, 20, 20);
            g.drawString(pingText, screenWidth - 100, 20);
        }
    }

    public void dispose() { // Отключение клиента и сервера
        if(networkClient != null) networkClient.disconnect();

        if(networkServer != null) networkServer.stop();
    }

    public LocalPlayer getLocalPlayer() {
        return  localPlayer;
    }

    public Collection<Player> getAllPlayers() {
        return players.values();
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }
}

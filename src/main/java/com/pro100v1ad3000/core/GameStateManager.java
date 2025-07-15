package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.entities.players.LocalPlayer;
import main.java.com.pro100v1ad3000.entities.players.Player;
import main.java.com.pro100v1ad3000.entities.players.RemotePlayer;
import main.java.com.pro100v1ad3000.network.client.NetworkClient;
import main.java.com.pro100v1ad3000.network.packets.PlayerMovePacket;
import main.java.com.pro100v1ad3000.network.packets.ReconnectPacket;
import main.java.com.pro100v1ad3000.network.packets.ServerShutdownPacket;
import main.java.com.pro100v1ad3000.network.server.NetworkServer;
import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.utils.Config;
import main.java.com.pro100v1ad3000.utils.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class GameStateManager {

    private final Map<Integer, Player> players = new ConcurrentHashMap<>();
    private LocalPlayer localPlayer;
    private NetworkClient networkClient;
    private NetworkServer networkServer;
    private boolean isMultiplayer = false;
    private boolean isHost = false;
    private String serverAddress;

    private static final int MAX_RECONNECT_ATTEMPTS = 5;
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
        networkServer = new NetworkServer(12345, this::handleServerPacket);
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

            Logger.info("Player connected to server id: " + playerId);
            // Логика добавления игрока и отправки информации о новом игроке на сервер
            networkClient.sendPacket(new PlayerMovePacket(playerId, 0, 0));

        } else {
            Logger.error("Failed to connect to server");
        }
    }

    private void handleServerPacket(NetworkServer.ClientHandler client, Object packet) { // Обрабатывает пакет на сервере
        // Обрабатывает входящие пакеты на стороне сервера
        // Логика обработки пакетов от клиентов
        if(packet instanceof PlayerMovePacket) {
            PlayerMovePacket movePacket = (PlayerMovePacket) packet;
            updatePlayerPosition(movePacket.getPlayerId(), movePacket.getX(), movePacket.getX());
            networkServer.broadcast(packet, client);
        }
    }

    private void handleClientPacket(Object packet) { // обрабатывает пакет на клиенте
        // Обрабатывает входящие пакеты на стороне клиента
        // Логика обработки пакетов от сервера

        if (packet instanceof ServerShutdownPacket) {
            Logger.info("Server is shutting down. Disconnection...");
            // Можно уведомить пользователя
            return;
        }

        if(packet instanceof PlayerMovePacket) {
            PlayerMovePacket movePacket = (PlayerMovePacket) packet;
            updatePlayerPosition(movePacket.getPlayerId(), movePacket.getX(), movePacket.getY());
        } else if (packet instanceof ReconnectPacket) {
            ReconnectPacket reconnectPacket = (ReconnectPacket) packet;
            handleReconnect(reconnectPacket);
        }

    }

    private void handleReconnect(ReconnectPacket packet) {
        Player player = players.get(packet.getPlayerId());
        if(player != null) {
            player.setPosition(packet.getX(), player.getY());
            Logger.info("Player " + packet.getPlayerId() + " reconnected");
        }
    }

    private void updatePlayerPosition(int playerId, float x, float y) {
        if(playerId == localPlayer.getId()) return;

        Player player = players.get(playerId);
        if(player == null) {
            player = new RemotePlayer(playerId, x, y);
            players.put(playerId, player);
        } else {
            player.setPosition(x, y);
        }
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

        g.setColor(Color.WHITE);
        String text = (isHost) ? "Server" : "Client";
        g.drawString(text, 20, 20);
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

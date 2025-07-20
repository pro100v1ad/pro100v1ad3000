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
import java.util.concurrent.ConcurrentHashMap;


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

    public void initSinglePlayer() {
        // Инициализирует игру в режиме одного игрока
        isMultiplayer = false;
        players.clear();
        localPlayer = new LocalPlayer(1, 0, 0, null);
        players.put(localPlayer.getId(), localPlayer);

        // Создаем одиночную игру
    }

    public void initMultiplayer(boolean isHost, String hostAddress) {
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

    private void startServer() {
        // Создает и запускает сервер на порту 12345 с обработчиком входящих пакетов
        networkServer = new NetworkServer(Config.SERVER_PORT, this::handleServerPacket);
        networkServer.start();
        Logger.info("Server started");
    }

    private void startClient() {
        // Создает и подключает клиент к серверу с заданными параметрами подключения
        networkClient = new NetworkClient(
                isHost ? "localhost" : serverAddress,
                12345,
                MAX_RECONNECT_ATTEMPTS,
                RECONNECT_DELAY_MS,
                this::handleClientPacket
        );

        if(networkClient.connect()) {
            int playerId = 0;
            localPlayer = new LocalPlayer(playerId, 0, 0, networkClient);
            players.put(localPlayer.getId(), localPlayer);

            networkClient.sendPacket(new PlayerConnectPacket(playerId, 0, 0));
            Logger.info("Player connected to server id: " + playerId);

        } else {
            Logger.error("Failed to connect to server");
        }
    }

    private void handleServerPacket(NetworkServer.ClientHandler client, Object packet) {
        // Обрабатывает входящие пакеты на стороне сервера

    }

    private void handleClientPacket(Object packet) {
        // Обрабатывает входящие пакеты на стороне клиента


    }


    public void update(float deltaTime) {
        // Обновляет состояние игры на основе времени, прошедшего с последнего кадра
    }

    public void render(Graphics2D g, int currentWidth, int currentHeight) {
        // Очистка экрана
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, currentWidth, currentHeight);

        // рассчитываем масштаб
        float scaleX = currentWidth / (float)Config.BASE_WIDTH;
        float scaleY = currentHeight / (float)Config.BASE_HEIGHT;


        AffineTransform originalTransform = g.getTransform(); // Сохраняем оригинальные трансформации
        g.scale(scaleX, scaleY); // Применяем масштабирование
        renderGameObjects(g);// Отрисовка игровых объектов (в координатах 800х600)
        g.setTransform(originalTransform);// Восстанавливаем оригинальные трансформации
        renderUI(g, currentWidth, currentHeight); // Отрисовка интерфейса

    }

    private void renderGameObjects(Graphics2D g) {

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


}

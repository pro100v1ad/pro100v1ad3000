package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.entities.players.LocalPlayer;
import main.java.com.pro100v1ad3000.entities.players.Player;
import main.java.com.pro100v1ad3000.network.client.NetworkClient;
import main.java.com.pro100v1ad3000.network.packets.*;
import main.java.com.pro100v1ad3000.network.server.NetworkServer;
import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.utils.Config;
import main.java.com.pro100v1ad3000.utils.Logger;

import java.awt.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class GamePlayStateManager {

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
    private final AssetManager assetManager;

    public GamePlayStateManager(InputManager inputManager, AssetManager assetManager) {
        this.inputManager = inputManager;
        this.assetManager = assetManager;
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

    public void update() {

    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {

    }

    public void dispose() { // Отключение клиента и сервера
        if(networkClient != null) networkClient.disconnect();
        if(networkServer != null) networkServer.stop();
    }


}

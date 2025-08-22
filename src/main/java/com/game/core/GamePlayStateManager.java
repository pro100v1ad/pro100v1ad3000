package main.java.com.game.core;

import main.java.com.game.world.World;
import main.java.com.game.world.WorldConfig;
import main.java.com.game.world.entities.players.LocalPlayer;
import main.java.com.game.network.client.NetworkClient;
import main.java.com.game.network.packets.*;
import main.java.com.game.network.server.NetworkServer;
import main.java.com.game.utils.Config;
import main.java.com.game.utils.Logger;

import java.awt.*;


public class GamePlayStateManager {

    private LocalPlayer localPlayer;
    private NetworkClient networkClient;
    private NetworkServer networkServer;
    private boolean isHost = false;
    private String serverAddress;

    private WorldConfig worldConfig;
    private World world;

    private static final int MAX_RECONNECT_ATTEMPTS = 8;
    private static final int RECONNECT_DELAY_MS = 5000;

    public GamePlayStateManager(WorldConfig worldConfig) {
        this.worldConfig = worldConfig;
        initSinglePlayer();
    }

    public GamePlayStateManager(WorldConfig worldConfig, boolean isHost, String hostAddress) {
        this.worldConfig = worldConfig;
        initMultiplayer(isHost, hostAddress);
    }

    public void initSinglePlayer() {
        // Создаем одиночную игру
        Logger.info("Creating a single player game...");
        world = new World(worldConfig);

    }

    public void initMultiplayer(boolean isHost, String hostAddress) {
        // Инициализирует многопользовательскую игру, устанавливая роль хоста и адрес сервера
        this.isHost = isHost;
        this.serverAddress = hostAddress;

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
//            localPlayer = new LocalPlayer(playerId, 0, 0);

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
        if(world != null) {
            world.update();
        }
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {

    }

    public void dispose() { // Отключение клиента и сервера
        if(networkClient != null) networkClient.disconnect();
        if(networkServer != null) networkServer.stop();
    }


}

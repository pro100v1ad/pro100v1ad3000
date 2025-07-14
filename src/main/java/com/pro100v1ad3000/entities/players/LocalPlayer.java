package main.java.com.pro100v1ad3000.entities.players;

import main.java.com.pro100v1ad3000.network.client.NetworkClient;
import main.java.com.pro100v1ad3000.network.packets.PlayerMovePacket;
import main.java.com.pro100v1ad3000.network.packets.ReconnectPacket;

public class LocalPlayer extends Player {

    private final NetworkClient networkClient;

    public LocalPlayer(int id, float x, float y, NetworkClient networkClient) {

        super(id, x, y);
        this.networkClient = networkClient;

    }

    public void move(float dx, float dy) {
        x += dx;
        y += dy;

        if(networkClient != null && networkClient.isConnected()) {
            networkClient.sendPacket(new PlayerMovePacket(id, x, y));

        }
    }

    @Override
    public void update(float deltaTime) {
        // Логика обновления для локального игрока
    }

    public void onReconnect() {
        if (networkClient != null && networkClient.isConnected()) {
            networkClient.sendPacket(new ReconnectPacket(id, x, y));
        }
    }

}

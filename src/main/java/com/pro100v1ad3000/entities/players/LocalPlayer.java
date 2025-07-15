package main.java.com.pro100v1ad3000.entities.players;

import main.java.com.pro100v1ad3000.network.client.NetworkClient;
import main.java.com.pro100v1ad3000.network.packets.PlayerConnectPacket;
import main.java.com.pro100v1ad3000.network.packets.PlayerMovePacket;
import main.java.com.pro100v1ad3000.network.packets.ReconnectPacket;

import java.awt.*;

public class LocalPlayer extends Player {

    private final NetworkClient networkClient;

    public LocalPlayer(int id, float x, float y, NetworkClient networkClient) {

        super(id, x, y);
        this.networkClient = networkClient;

        if(networkClient != null && networkClient.isConnected()) {
            // Отправляем пакет подключения сразу после создания
            networkClient.sendPacket(new PlayerConnectPacket(id, x, y));
            networkClient.sendPacket(new PlayerMovePacket(id, x, y));
        }

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

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(100, 100, 10));
        g.fillRect((int)x, (int)y, 20, 20);
    }

}

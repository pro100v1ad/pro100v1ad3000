package main.java.com.pro100v1ad3000.network.packets;

import java.io.Serializable;

public class ReconnectPacket implements Serializable {

    private final int playerId;
    private final float x;
    private final float y;

    public ReconnectPacket(int playerId, float x, float y) {

        this.playerId = playerId;
        this.x = x;
        this.y = y;

    }

    public int getPlayerId() { return playerId; }
    public float getX() { return x; }
    public float getY() { return y; }

}

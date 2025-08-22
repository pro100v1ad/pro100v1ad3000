package main.java.com.game.network.packets;

import java.io.Serializable;

public class PlayerDisconnectedPacket implements Serializable {

    private final int playerId;

    public PlayerDisconnectedPacket(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

}

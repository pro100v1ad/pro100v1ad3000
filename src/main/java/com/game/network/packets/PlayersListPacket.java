package main.java.com.game.network.packets;

import java.io.Serializable;
import java.util.List;

public class PlayersListPacket implements Serializable {

    private final List<PlayerData> players;

    public PlayersListPacket(List<PlayerData> players) {
        this.players = players;
    }

    public List<PlayerData> getPlayers() {
        return players;
    }

}

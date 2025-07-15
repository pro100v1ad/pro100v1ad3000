package main.java.com.pro100v1ad3000.network.packets;

import java.io.Serializable;

public class PlayerData implements Serializable {

    private final int id;
    private final float x;
    private final float y;

    public PlayerData(int id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


}

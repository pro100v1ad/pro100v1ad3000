package main.java.com.game.world.entities.players;

import java.awt.*;

public class RemotePlayer extends Player {

    public RemotePlayer(int id, float x, float y) {
        super(id, x, y);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(100, 100, 10));
        g.fillRect((int)x, (int)y, 20, 20);
    }

}

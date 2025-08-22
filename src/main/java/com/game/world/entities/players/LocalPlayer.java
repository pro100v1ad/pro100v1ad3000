package main.java.com.game.world.entities.players;

import main.java.com.game.world.entities.players.utils.PlayerInfo;

import java.awt.*;

public class LocalPlayer extends Player {


    public LocalPlayer(PlayerInfo playerInfo) {
        super(playerInfo.getId(), 0, 0);

    }

    @Override
    public void update(float deltaTime) {
        // Логика обновления для локального игрока
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(100, 100, 10));
        g.fillRect((int)x, (int)y, 20, 20);
    }

}

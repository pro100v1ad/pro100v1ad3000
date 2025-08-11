package main.java.com.pro100v1ad3000.ui.menus.playerSettings;

import main.java.com.pro100v1ad3000.entities.players.utils.PlayerBody;
import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;

import java.awt.*;

public class PlayersAppearance {

    private final PlayerBody playerBody;

    private int posX, posY, width, height;

    public PlayersAppearance(int posX, int posY, int width, int height) {


        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        playerBody = new PlayerBody(posX + width/6, posY + height/6);

    }

    public void update(int currentWidth, int currentHeight) {

    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        playerBody.draw(g);
    }

}

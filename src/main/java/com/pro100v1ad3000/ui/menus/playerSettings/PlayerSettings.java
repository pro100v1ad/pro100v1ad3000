package main.java.com.pro100v1ad3000.ui.menus.playerSettings;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.entities.players.utils.PlayerDataManager;

import java.awt.*;

public class PlayerSettings {

    private final Nicknames nicknames;
    private final PlayersAppearance playersAppearance;

    private final int posX, posY, width, height;

    private PlayerDataManager playerDataManager;

    public PlayerSettings(int posX, int posY, int width, int height) {

        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        int nicknameX = posX;
        int nicknameY = posY + height*6/7;
        int nicknameWidth = width;
        int nicknameHeight = height/7;
        nicknames = new Nicknames(nicknameX, nicknameY, nicknameWidth, nicknameHeight);
        playersAppearance = new PlayersAppearance(posX, posY, width, height);


    }

    public void update(int currentWidth, int currentHeight) {
        nicknames.update(currentWidth, currentHeight);
        playersAppearance.update(currentWidth, currentHeight);

        playerDataManager = nicknames.getPlayerDataManager();
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        g.setColor(Color.WHITE);
        g.drawRect(posX, posY, width, height);

        nicknames.draw(g, currentWidth, currentHeight);
        playersAppearance.draw(g, currentWidth, currentHeight);
    }

}

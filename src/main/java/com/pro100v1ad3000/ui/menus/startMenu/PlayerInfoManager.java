package main.java.com.pro100v1ad3000.ui.menus.startMenu;

import main.java.com.pro100v1ad3000.ui.utils.InputFields;
import main.java.com.pro100v1ad3000.utils.Logger;
import main.java.com.pro100v1ad3000.world.entities.players.utils.PlayerInfo;
import main.java.com.pro100v1ad3000.world.entities.players.utils.PlayerInfoSaveManager;

import java.awt.*;

public class PlayerInfoManager {

    private final InputFields fields;
    private final int posX, posY, width, height;

    private final PlayerInfoSaveManager players;

    public PlayerInfoManager(int posX, int posY, int width, int height) {

        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        fields = new InputFields(posX, posY + height*9/10, width, height/10);

        players = PlayerInfoSaveManager.getInstance();
        PlayerInfo player = players.getActivePlayer();
        fields.setText(player.getNickname());
    }

    public void update(int currentWidth, int currentHeight) {
        boolean flag = fields.isActiveTextArea();
        fields.update(currentWidth, currentHeight);
        if(flag && !fields.isActiveTextArea()) {
            players.addPlayer(new PlayerInfo(fields.getText()));
        }
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        fields.draw(g);
        g.setColor(Color.WHITE);
        g.drawRect(posX, posY, width, height);
    }
}

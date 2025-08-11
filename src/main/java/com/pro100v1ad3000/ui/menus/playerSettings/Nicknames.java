package main.java.com.pro100v1ad3000.ui.menus.playerSettings;

import main.java.com.pro100v1ad3000.entities.players.utils.PlayerDataManager;
import main.java.com.pro100v1ad3000.entities.players.utils.PlayerSettingsManager;
import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.ui.utils.InputFields;

import java.awt.*;

public class Nicknames {

    private final int nicknameX, nicknameY, nicknameWidth, nicknameHeight;

    private boolean isActiveInputField;
    private InputFields inputFieldsNickname;
    private PlayerDataManager playerDataManager;

    public Nicknames(int posX, int posY, int width, int height) {

        this.nicknameX = posX;
        this.nicknameY = posY;
        this.nicknameWidth = width;
        this.nicknameHeight = height;

        playerDataManager = PlayerSettingsManager.getActivePlayerData();
        if(playerDataManager == null) {
            playerDataManager = new PlayerDataManager("unknown");
        }

        if (!"unknown".equals(playerDataManager.getNickname())) {
            playerDataManager = PlayerSettingsManager.getOrSetPlayerInfo(playerDataManager.getNickname());
        }
        inputFieldsNickname = new InputFields(nicknameX, nicknameY, nicknameWidth, nicknameHeight);
        inputFieldsNickname.setText(playerDataManager.getNickname());
        this.isActiveInputField = false;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public void update(int currentWidth, int currentHeight) {
        inputFieldsNickname.update(currentWidth, currentHeight);

        if(isActiveInputField && !inputFieldsNickname.isActiveTextArea() && !inputFieldsNickname.getText().equals(" ")) {
            String nickname = inputFieldsNickname.getText();

            playerDataManager = PlayerSettingsManager.getOrSetPlayerInfo(nickname);

        }

        isActiveInputField = inputFieldsNickname.isActiveTextArea();

    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {

        g.setColor(Color.WHITE);
        g.drawRect(nicknameX, nicknameY, nicknameWidth, nicknameHeight);
        inputFieldsNickname.draw(g);
    }

}

package main.java.com.pro100v1ad3000.ui.menus.playerSettings;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.ui.menus.playerSettings.utils.PlayerDataManager;
import main.java.com.pro100v1ad3000.ui.menus.playerSettings.utils.PlayerSettingsManager;
import main.java.com.pro100v1ad3000.ui.utils.InputFields;

import java.awt.*;

public class PlayerSettings {

    private final InputManager inputManager;
    private final AssetManager assetManager;

    private InputFields inputFieldsNickname;

    private final int posX, posY, width, height;
    private final int nicknameX, nicknameY, nicknameWidth, nicknameHeight;

    private boolean isActiveInputField;

    private PlayerDataManager playerDataManager;

    public PlayerSettings(InputManager inputManager, AssetManager assetManager, int posX, int posY, int width, int height) {

        this.inputManager = inputManager;
        this.assetManager = assetManager;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        this.nicknameX = posX;
        this.nicknameY = posY + height*6/7;
        this.nicknameWidth = width;
        this.nicknameHeight = height/7;

        playerDataManager = PlayerSettingsManager.getActivePlayerData();
        if(playerDataManager == null) {
            playerDataManager = new PlayerDataManager("unknown");
        }

        if (!"unknown".equals(playerDataManager.getNickname())) {
            playerDataManager = PlayerSettingsManager.getOrSetPlayerInfo(playerDataManager.getNickname());
        }
        inputFieldsNickname = new InputFields(inputManager, assetManager, nicknameX, nicknameY, nicknameWidth, nicknameHeight);
        inputFieldsNickname.setText(playerDataManager.getNickname());
        this.isActiveInputField = false;


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
        g.drawRect(posX, posY, width, height);

        g.setColor(Color.WHITE);
        g.drawRect(posX, posY + height*6/7, width, height/7);
        inputFieldsNickname.draw(g);
    }

}

package main.java.com.pro100v1ad3000.ui.menus.playerSettings;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.ui.menus.playerSettings.utils.PlayerSettingsManager;

import java.awt.*;

public class PlayerSettings {



    private final InputManager inputManager;
    private final AssetManager assetManager;

    private final int posX, posY, width, height;

    private final PlayerSettingsManager playerSettingsManager;

    public PlayerSettings(InputManager inputManager, AssetManager assetManager, int posX, int posY, int width, int height) {

        this.inputManager = inputManager;
        this.assetManager = assetManager;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;

        this.playerSettingsManager = new PlayerSettingsManager();

    }

    public void update() {

    }

    public void draw(Graphics2D g) {

    }

    public void dispose() {
        playerSettingsManager.dispose();
    }
}

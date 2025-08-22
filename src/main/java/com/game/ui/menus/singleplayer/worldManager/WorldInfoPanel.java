package main.java.com.game.ui.menus.singleplayer.worldManager;

import main.java.com.game.systems.language.LanguageManager;
import main.java.com.game.ui.menus.singleplayer.WorldsChoiceManager;
import main.java.com.game.ui.utils.RoundedRectangleButton;
import main.java.com.game.world.WorldConfig;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WorldInfoPanel {
    private final int posX, posY, width, height;
    private final WorldConfig worldConfig;
    private boolean isVisible;

    private final Map<String, RoundedRectangleButton> buttons = new HashMap<>();

    private final int DELETE_WORLD_BUTTON_X, DELETE_WORLD_BUTTON_Y, DELETE_WORLD_BUTTON_WIDTH, DELETE_WORLD_BUTTON_HEIGHT;
    private final int PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT;

    private final WorldsChoiceManager worldsManager;

    public WorldInfoPanel(int posX, int posY, int width, int height, WorldConfig worldInfo, WorldsChoiceManager worldsManager) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.worldConfig = worldInfo;
        this.isVisible = false;
        this.worldsManager = worldsManager;

        DELETE_WORLD_BUTTON_WIDTH = width/10;
        DELETE_WORLD_BUTTON_HEIGHT = height/10;
        DELETE_WORLD_BUTTON_X = posX + width - DELETE_WORLD_BUTTON_WIDTH;
        DELETE_WORLD_BUTTON_Y = posY;

        PLAY_BUTTON_WIDTH = width/3;
        PLAY_BUTTON_HEIGHT = height/10;
        PLAY_BUTTON_X = posX + width - PLAY_BUTTON_WIDTH;
        PLAY_BUTTON_Y = posY + height - PLAY_BUTTON_HEIGHT;

        setButtons();
    }

    private void setButtons() {
        buttons.clear();

        int cornerRadius = 15;

        buttons.put("deleteWorldButton", new RoundedRectangleButton(DELETE_WORLD_BUTTON_X, DELETE_WORLD_BUTTON_Y, DELETE_WORLD_BUTTON_WIDTH, DELETE_WORLD_BUTTON_HEIGHT, cornerRadius, null, null, null));
        buttons.put("playButton", new RoundedRectangleButton(PLAY_BUTTON_X, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT, cornerRadius, null, null, null));

        setButtonsText();
    }

    private void setButtonsText() {
        buttons.get("playButton").setText(LanguageManager.getText("menu.singleplayerMenu.buttons.play_button"), 16, Color.BLUE);
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public WorldConfig getWorldConfig() {
        return worldConfig;
    }

    public void update(int currentWidth, int currentHeight) {
        for (Map.Entry<String, RoundedRectangleButton> entry : buttons.entrySet()) {
            RoundedRectangleButton button = entry.getValue();
            if (button.update(currentWidth, currentHeight)) {
                switch (entry.getKey()) {
                    case "deleteWorldButton": {
                        worldsManager.deleteWorld(worldConfig);
                        break;
                    }
                    case "playButton": {
                        worldsManager.startGame(worldConfig);
    // Когда нажата клавиша запуска игры
                        break;
                    }
                }
            }
        }
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        if (!isVisible) return;

        g.setColor(new Color(220, 220, 220, 200));
        g.fillRect(posX, posY, width, height);

        g.setColor(Color.BLACK);
        g.drawString("World Name: " + worldConfig.getName(), posX + 10, posY + 30);
        g.drawString("Seed: " + worldConfig.getSeed(), posX + 10, posY + 60);

        for (RoundedRectangleButton button : buttons.values()) {
            button.draw(g, currentWidth, currentHeight);
        }
    }
}

package main.java.com.pro100v1ad3000.ui.menus;

import main.java.com.pro100v1ad3000.systems.language.LanguageManager;
import main.java.com.pro100v1ad3000.ui.utils.RoundedRectangleButton;
import main.java.com.pro100v1ad3000.utils.Config;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;

public class StartMenu extends Menu{

    private final Map<String, RoundedRectangleButton> buttons = new HashMap<>();

    public StartMenu() {
        super();
        setButtons();
    }

    private void setButtons() {

        buttons.clear();

        int rectWidth = Config.BASE_WIDTH/5;
        int rectHeight = Config.BASE_HEIGHT/15;
        int rectX = Config.BASE_WIDTH/2 - rectWidth/2;
        int rectY = Config.BASE_HEIGHT/2 - rectHeight/2;
        int cornerRadius = 15;

        int curRectY = (int)(rectY - (float)rectHeight*1.2);
        buttons.put("singlePlayerButton", new RoundedRectangleButton(rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        buttons.put("multiplayerButton", new RoundedRectangleButton(rectX, rectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        curRectY = (int)(rectY + (float)rectHeight*1.2);
        buttons.put("achievementsButton", new RoundedRectangleButton(rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        curRectY = (int)(rectY + (float)rectHeight*2.4);
        buttons.put("settingsButton", new RoundedRectangleButton(rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        int curRectX = (int)(rectX - (float)(rectHeight * 1.1));
        buttons.put("languageButton", new RoundedRectangleButton(curRectX, curRectY, rectHeight, rectHeight, cornerRadius, null, null, null));

        curRectY = (int)(rectY + (float)rectHeight*3.6);
        buttons.put("exitButton", new RoundedRectangleButton(rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        setButtonsText();
    }

    private void setButtonsText() {
        buttons.get("singlePlayerButton").setText(LanguageManager.getText("menu.startMenu.buttons.singlePlayer_button"), 16, Color.BLUE);
        buttons.get("multiplayerButton").setText(LanguageManager.getText("menu.startMenu.buttons.multiplayer_button"), 16, Color.BLUE);
        buttons.get("achievementsButton").setText(LanguageManager.getText("menu.startMenu.buttons.achievements_button"), 16, Color.BLUE);
        buttons.get("settingsButton").setText(LanguageManager.getText("menu.startMenu.buttons.settings_button"), 16, Color.BLUE);
        buttons.get("exitButton").setText(LanguageManager.getText("menu.startMenu.buttons.exit_button"), 16, Color.BLUE);

    }


    @Override
    public void update(int currentWidth, int currentHeight) {
        for (Map.Entry<String, RoundedRectangleButton> entry : buttons.entrySet()) {
            RoundedRectangleButton button = entry.getValue();
            if (button.update(currentWidth, currentHeight)) {
                switch (entry.getKey()) {
                    case "singlePlayerButton": {
                        MenuManager.setCurrentMenu("SingleplayerMenu");
                        break;
                    }
                    case "multiplayerButton": {
                        MenuManager.setCurrentMenu("MultiplayerMenu");
                        break;
                    }
                    case "achievementsButton": {
                        MenuManager.setCurrentMenu("AchievementsMenu");
                        break;
                    }
                    case "settingsButton": {
                        MenuManager.setCurrentMenu("SettingsMenu");
                        break;
                    }
                    case "languageButton": {
                        MenuManager.setCurrentMenu("LanguageMenu");
                        break;
                    }
                    case "exitButton": {
                        exit(0);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        for (RoundedRectangleButton button : buttons.values()) {
            button.draw(g, currentWidth, currentHeight);
        }
    }
}

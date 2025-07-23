package main.java.com.pro100v1ad3000.ui.menus;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.systems.language.LanguageManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.ui.utils.RoundedRectangleButton;
import main.java.com.pro100v1ad3000.utils.Config;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LanguageMenu {

    private boolean isVisible;

    private final InputManager inputManager;
    private final AssetManager assetManager;
    private final LanguageManager languageManager;
    private final Menus menus;

    private Map<String, RoundedRectangleButton> buttons;

    public LanguageMenu(InputManager inputManager, AssetManager assetManager, LanguageManager languageManager, Menus menus) {
        this.inputManager = inputManager;
        this.assetManager = assetManager;
        this.languageManager = languageManager;
        this.menus = menus;

        buttons = new HashMap<>();

        isVisible = false;

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
        buttons.put("russianButton", new RoundedRectangleButton(inputManager, rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));
        buttons.put("englishButton", new RoundedRectangleButton(inputManager, rectX, rectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        curRectY = (int)(rectY + (float)rectHeight*1.2);
        buttons.put("doneButton", new RoundedRectangleButton(inputManager, rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        setButtonsText();
    }

    private void setButtonsText() {
        buttons.get("russianButton").setText(languageManager.getText("menu.languageMenu.buttons.russian_button"), 16, Color.BLUE);
        buttons.get("englishButton").setText(languageManager.getText("menu.languageMenu.buttons.english_button"), 16, Color.BLUE);
        buttons.get("doneButton").setText(languageManager.getText("menu.languageMenu.buttons.done_button"), 16, Color.BLUE);

    }

    public void setVisible(boolean isVisible) {
        if(isVisible) setButtonsText();
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void update(int currentWidth, int currentHeight) {

        for(Map.Entry<String, RoundedRectangleButton> entry : buttons.entrySet()) {
            RoundedRectangleButton button = entry.getValue();
            if(button.update(currentWidth, currentHeight)) {
                switch (entry.getKey()) {
                    case "englishButton": {
                        languageManager.setLanguage("en");
                        menus.showLanguageMenu();
                        break;
                    }
                    case "russianButton": {
                        languageManager.setLanguage("ru");
                        menus.showLanguageMenu();
                        break;
                    }
                    case "doneButton": {
                        menus.showStartMenu();
                        break;
                    }
                }
            }
        }

    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        for(RoundedRectangleButton button : buttons.values()) {
            button.draw(g, currentWidth, currentHeight);
        }
    }
}

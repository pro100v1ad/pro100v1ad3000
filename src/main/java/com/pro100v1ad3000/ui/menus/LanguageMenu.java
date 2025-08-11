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

    private final Menus menus;

    private Map<String, RoundedRectangleButton> buttons;

    public LanguageMenu(Menus menus) {
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
        buttons.put("russianButton", new RoundedRectangleButton(rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));
        buttons.put("englishButton", new RoundedRectangleButton(rectX, rectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        curRectY = (int)(rectY + (float)rectHeight*1.2);
        buttons.put("doneButton", new RoundedRectangleButton(rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        setButtonsText();
    }

    private void setButtonsText() {
        buttons.get("russianButton").setText(LanguageManager.getText("menu.languageMenu.buttons.russian_button"), 16, Color.BLUE);
        buttons.get("englishButton").setText(LanguageManager.getText("menu.languageMenu.buttons.english_button"), 16, Color.BLUE);
        buttons.get("doneButton").setText(LanguageManager.getText("menu.languageMenu.buttons.done_button"), 16, Color.BLUE);

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
                        LanguageManager.setLanguage("en");
                        menus.showLanguageMenu();
                        break;
                    }
                    case "russianButton": {
                        LanguageManager.setLanguage("ru");
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

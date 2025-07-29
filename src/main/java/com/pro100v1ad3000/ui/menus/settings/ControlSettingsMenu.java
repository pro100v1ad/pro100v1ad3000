package main.java.com.pro100v1ad3000.ui.menus.settings;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.systems.language.LanguageManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.ui.menus.Menus;
import main.java.com.pro100v1ad3000.ui.utils.RoundedRectangleButton;
import main.java.com.pro100v1ad3000.utils.Config;
import main.java.com.pro100v1ad3000.utils.Logger;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ControlSettingsMenu {

    private boolean isVisible;

    private final InputManager inputManager;
    private final AssetManager assetManager;
    private final LanguageManager languageManager;
    private final Menus menus;

    private Map<String, RoundedRectangleButton> buttons;

    public ControlSettingsMenu(InputManager inputManager, AssetManager assetManager, LanguageManager languageManager, Menus menus) {

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
        buttons.put("w_button", new RoundedRectangleButton(inputManager, rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        buttons.put("a_button", new RoundedRectangleButton(inputManager, rectX, rectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        curRectY = (int)(rectY + (float)rectHeight*1.2);
        buttons.put("s_button", new RoundedRectangleButton(inputManager, rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        curRectY = (int)(rectY + (float)rectHeight*2.4);
        buttons.put("d_button", new RoundedRectangleButton(inputManager, rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        curRectY = (int)(rectY + (float)rectHeight*3.6);
        buttons.put("doneButton", new RoundedRectangleButton(inputManager, rectX, curRectY, rectWidth, rectHeight, cornerRadius, null, null, null));

        setButtonsText();
    }

    private void setButtonsText() {
        buttons.get("w_button").setText(languageManager.getText("menu.settingsMenu.controlMenu.buttons.w_button"), 16, Color.BLUE);
        buttons.get("a_button").setText(languageManager.getText("menu.settingsMenu.controlMenu.buttons.a_button"), 16, Color.BLUE);
        buttons.get("s_button").setText(languageManager.getText("menu.settingsMenu.controlMenu.buttons.s_button"), 16, Color.BLUE);
        buttons.get("d_button").setText(languageManager.getText("menu.settingsMenu.controlMenu.buttons.d_button"), 16, Color.BLUE);
        buttons.get("doneButton").setText(languageManager.getText("menu.settingsMenu.controlMenu.buttons.doneButton"), 16, Color.BLUE);

    }

    public void setVisible(boolean isVisible) {
        if(isVisible) setButtonsText();
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void update(int currentWidth, int currentHeight) {

        if(buttons != null) {
            for (Map.Entry<String, RoundedRectangleButton> entry : buttons.entrySet()) {
                RoundedRectangleButton button = entry.getValue();
                if (button.update(currentWidth, currentHeight)) {
                    switch (entry.getKey()) {
                        case "w_button": {
                            break;
                        }
                        case "a_button": {
                            break;
                        }
                        case "s_button": {
                            break;
                        }
                        case "d_button": {
                            break;
                        }
                        case "doneButton": {
                            menus.showSettingsMenu();
                            break;
                        }
                    }
                }
            }
        }

    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        if(buttons != null) {
            for (RoundedRectangleButton button : buttons.values()) {
                button.draw(g, currentWidth, currentHeight);
            }
        }
    }


}

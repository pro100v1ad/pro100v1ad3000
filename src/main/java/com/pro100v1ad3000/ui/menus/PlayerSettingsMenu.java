package main.java.com.pro100v1ad3000.ui.menus;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.systems.language.LanguageManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.ui.utils.RoundedRectangleButton;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class PlayerSettingsMenu {

    private boolean isVisible;

    private final InputManager inputManager;
    private final AssetManager assetManager;
    private final LanguageManager languageManager;
    private final Menus menus;

    private Map<String, RoundedRectangleButton> buttons;



    public PlayerSettingsMenu(InputManager inputManager, AssetManager assetManager, LanguageManager languageManager, Menus menus) {
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


        setButtonsText();
    }

    private void setButtonsText() {

    }

    public void setVisible(boolean isVisible) {
        if(isVisible) setButtonsText();
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void update(int currentWidth, int currentHeight) {

    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {

    }
}

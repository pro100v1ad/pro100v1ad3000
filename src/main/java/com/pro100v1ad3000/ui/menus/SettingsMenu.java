package main.java.com.pro100v1ad3000.ui.menus;

import main.java.com.pro100v1ad3000.systems.language.LanguageManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.systems.InputManager;

import java.awt.*;

public class SettingsMenu {

    private boolean isVisible;

    private final InputManager inputManager;
    private final AssetManager assetManager;
    private final LanguageManager languageManager;
    private final Menus menus;

    public SettingsMenu(InputManager inputManager, AssetManager assetManager, LanguageManager languageManager, Menus menus) {

        this.inputManager = inputManager;
        this.assetManager = assetManager;
        this.languageManager = languageManager;
        this.menus = menus;

        isVisible = false;

    }

    public void setVisible(boolean isVisible) {
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

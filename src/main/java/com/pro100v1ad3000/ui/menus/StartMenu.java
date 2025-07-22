package main.java.com.pro100v1ad3000.ui.menus;

import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.systems.InputManager;

import java.awt.*;

public class StartMenu {

    private boolean isVisible;

    private final InputManager inputManager;
    private final AssetManager assetManager;
    private final Menus menus;

    public StartMenu(InputManager inputManager, AssetManager assetManager, Menus menus) {

        this.inputManager = inputManager;
        this.assetManager = assetManager;
        this.menus = menus;

        isVisible = false;

    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void update() {

    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {

    }

}

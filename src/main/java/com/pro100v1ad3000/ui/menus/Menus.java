package main.java.com.pro100v1ad3000.ui.menus;

import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.systems.InputManager;

import java.awt.*;

public class Menus {

    private final InputManager inputManager;

    private final StartMenu startMenu;
    private final SettingsMenu settingsMenu;

    //  Будут разные виды меню, но их всех объединяет этот класс
    public Menus(InputManager inputManager, AssetManager assetManager) {
        this.inputManager = inputManager;

        startMenu = new StartMenu(inputManager, assetManager, this);
        settingsMenu = new SettingsMenu(inputManager, assetManager, this);

        showStartMenu();
    }

    public void showStartMenu() {
        startMenu.setVisible(true);
        settingsMenu.setVisible(false);
        inputManager.resetInputStates();
    }

    public void showSettingsMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(true);
        inputManager.resetInputStates();
    }

    public void update() {
        if(startMenu.isVisible()) startMenu.update();
        if(settingsMenu.isVisible()) settingsMenu.update();
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        if(startMenu.isVisible()) startMenu.draw(g, currentWidth, currentHeight);
        if(settingsMenu.isVisible()) settingsMenu.draw(g, currentWidth, currentHeight);
    }

}

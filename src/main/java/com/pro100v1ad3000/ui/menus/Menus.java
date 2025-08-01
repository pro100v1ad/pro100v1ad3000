package main.java.com.pro100v1ad3000.ui.menus;

import main.java.com.pro100v1ad3000.systems.language.LanguageManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.ui.menus.multiplayer.MultiplayerMenu;
import main.java.com.pro100v1ad3000.ui.menus.settings.ControlSettingsMenu;

import java.awt.*;

public class Menus {

    private final InputManager inputManager;

    private final StartMenu startMenu;
    private final SettingsMenu settingsMenu;
    private final ControlSettingsMenu controlSettingsMenu;
    private final MultiplayerMenu multiplayerMenu;

    private final LanguageMenu languageMenu;
    private final PlayerSettingsMenu playerSettingsMenu;

    //  Будут разные виды меню, но их всех объединяет этот класс
    public Menus(InputManager inputManager, AssetManager assetManager, LanguageManager languageManager) {
        this.inputManager = inputManager;

        startMenu = new StartMenu(inputManager, assetManager, languageManager, this);
        settingsMenu = new SettingsMenu(inputManager, assetManager, languageManager, this);
        languageMenu = new LanguageMenu(inputManager, assetManager, languageManager, this);
        multiplayerMenu = new MultiplayerMenu(inputManager, assetManager, languageManager, this);

        controlSettingsMenu = new ControlSettingsMenu(inputManager, assetManager, languageManager, this);
        playerSettingsMenu = new PlayerSettingsMenu(inputManager, assetManager, languageManager, this);

        showStartMenu();
    }

    public void showStartMenu() {
        startMenu.setVisible(true);
        settingsMenu.setVisible(false);
        languageMenu.setVisible(false);
        controlSettingsMenu.setVisible(false);
        multiplayerMenu.setVisible(false);
        playerSettingsMenu.setVisible(false);
        inputManager.resetInputStates();
    }

    public void showLanguageMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(false);
        languageMenu.setVisible(true);
        controlSettingsMenu.setVisible(false);
        multiplayerMenu.setVisible(false);
        playerSettingsMenu.setVisible(false);
        inputManager.resetInputStates();
    }

    public void showSettingsMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(true);
        languageMenu.setVisible(false);
        controlSettingsMenu.setVisible(false);
        multiplayerMenu.setVisible(false);
        playerSettingsMenu.setVisible(false);
        inputManager.resetInputStates();
    }

    public void showControlSettingsMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(false);
        languageMenu.setVisible(false);
        controlSettingsMenu.setVisible(true);
        multiplayerMenu.setVisible(false);
        playerSettingsMenu.setVisible(false);
        inputManager.resetInputStates();
    }

    public void showMultiplayerMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(false);
        languageMenu.setVisible(false);
        controlSettingsMenu.setVisible(false);
        multiplayerMenu.setVisible(true);
        playerSettingsMenu.setVisible(false);
        inputManager.resetInputStates();
    }

    public void showPlayerSettingsMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(false);
        languageMenu.setVisible(false);
        controlSettingsMenu.setVisible(false);
        multiplayerMenu.setVisible(false);
        playerSettingsMenu.setVisible(true);
        inputManager.resetInputStates();
    }

    public void update(int currentWidth, int currentHeight) {
        if(startMenu.isVisible()) startMenu.update(currentWidth, currentHeight);
        if(settingsMenu.isVisible()) settingsMenu.update(currentWidth, currentHeight);
        if(languageMenu.isVisible()) languageMenu.update(currentWidth, currentHeight);
        if(multiplayerMenu.isVisible()) multiplayerMenu.update(currentWidth, currentHeight);

        if(controlSettingsMenu.isVisible()) controlSettingsMenu.update(currentWidth, currentHeight);
        if(playerSettingsMenu.isVisible()) playerSettingsMenu.update(currentWidth, currentHeight);
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        if(startMenu.isVisible()) startMenu.draw(g, currentWidth, currentHeight);
        if(settingsMenu.isVisible()) settingsMenu.draw(g, currentWidth, currentHeight);
        if(languageMenu.isVisible()) languageMenu.draw(g, currentWidth, currentHeight);
        if(multiplayerMenu.isVisible()) multiplayerMenu.draw(g, currentWidth, currentHeight);

        if(controlSettingsMenu.isVisible()) controlSettingsMenu.draw(g, currentWidth, currentHeight);
        if(playerSettingsMenu.isVisible()) playerSettingsMenu.draw(g, currentWidth, currentHeight);


    }

}

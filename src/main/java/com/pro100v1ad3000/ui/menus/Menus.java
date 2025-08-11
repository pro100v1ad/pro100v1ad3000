package main.java.com.pro100v1ad3000.ui.menus;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.ui.menus.multiplayer.MultiplayerMenu;
import main.java.com.pro100v1ad3000.ui.menus.settings.ControlSettingsMenu;

import java.awt.*;

public class Menus {

    private final StartMenu startMenu;
    private final SettingsMenu settingsMenu;
    private final ControlSettingsMenu controlSettingsMenu;
    private final MultiplayerMenu multiplayerMenu;

    private final LanguageMenu languageMenu;
    private final PlayerSettingsMenu playerSettingsMenu;

    //  Будут разные виды меню, но их всех объединяет этот класс
    public Menus() {

        startMenu = new StartMenu(this);
        settingsMenu = new SettingsMenu(this);
        languageMenu = new LanguageMenu(this);
        multiplayerMenu = new MultiplayerMenu(this);

        controlSettingsMenu = new ControlSettingsMenu(this);
        playerSettingsMenu = new PlayerSettingsMenu(this);

        showStartMenu();
    }

    public void showStartMenu() {
        startMenu.setVisible(true);
        settingsMenu.setVisible(false);
        languageMenu.setVisible(false);
        controlSettingsMenu.setVisible(false);
        multiplayerMenu.setVisible(false);
        playerSettingsMenu.setVisible(false);
        InputManager.resetInputStates();
    }

    public void showLanguageMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(false);
        languageMenu.setVisible(true);
        controlSettingsMenu.setVisible(false);
        multiplayerMenu.setVisible(false);
        playerSettingsMenu.setVisible(false);
        InputManager.resetInputStates();
    }

    public void showSettingsMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(true);
        languageMenu.setVisible(false);
        controlSettingsMenu.setVisible(false);
        multiplayerMenu.setVisible(false);
        playerSettingsMenu.setVisible(false);
        InputManager.resetInputStates();
    }

    public void showControlSettingsMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(false);
        languageMenu.setVisible(false);
        controlSettingsMenu.setVisible(true);
        multiplayerMenu.setVisible(false);
        playerSettingsMenu.setVisible(false);
        InputManager.resetInputStates();
    }

    public void showMultiplayerMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(false);
        languageMenu.setVisible(false);
        controlSettingsMenu.setVisible(false);
        multiplayerMenu.setVisible(true);
        playerSettingsMenu.setVisible(false);
        InputManager.resetInputStates();
    }

    public void showPlayerSettingsMenu() {
        startMenu.setVisible(false);
        settingsMenu.setVisible(false);
        languageMenu.setVisible(false);
        controlSettingsMenu.setVisible(false);
        multiplayerMenu.setVisible(false);
        playerSettingsMenu.setVisible(true);
        InputManager.resetInputStates();
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

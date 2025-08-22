package main.java.com.game.ui.menus;

import main.java.com.game.systems.InputManager;
import main.java.com.game.ui.menus.achievements.AchievementsMenu;
import main.java.com.game.ui.menus.multiplayer.MultiplayerMenu;
import main.java.com.game.ui.menus.settings.SettingsMenu;
import main.java.com.game.ui.menus.singleplayer.SingleplayerMenu;
import main.java.com.game.utils.Logger;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MenuManager {
    private static final Map<String, Menu> menus = new HashMap<>();
    private static Menu currentMenu;

    static {

        // Инициализация всех меню
        menus.put("StartMenu", new StartMenu());
         // Стартовое меню
        menus.put("SingleplayerMenu", new SingleplayerMenu());
        menus.put("MultiplayerMenu", new MultiplayerMenu());
        menus.put("AchievementsMenu", new AchievementsMenu());
        menus.put("SettingsMenu", new SettingsMenu());

          // Для одиночной игры


          // Для сетевой игры

          // Для достижений

          // Для настроек

        setCurrentMenu("StartMenu");
    }

    private static void hideAllMenus() {
        for(Menu menu: menus.values()) {
            menu.isVisible = false;
        }
    }

    public static void setCurrentMenu(String menuName) {
        currentMenu = menus.get(menuName);
        hideAllMenus();
        if (currentMenu != null) {
            currentMenu.setVisible(true);
        } else {
            Logger.error("CurrentMenu isEmpty: " + menuName);
        }
    }

    public void update(int currentWidth, int currentHeight) {
        if (currentMenu != null && currentMenu.isVisible()) {
            currentMenu.update(currentWidth, currentHeight);
            InputManager.resetInputStates();
        }
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        if (currentMenu != null && currentMenu.isVisible()) {
            currentMenu.draw(g, currentWidth, currentHeight);
        }
    }
}


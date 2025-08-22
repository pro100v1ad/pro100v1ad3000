package main.java.com.game.ui.menus.singleplayer;

import main.java.com.game.systems.language.LanguageManager;
import main.java.com.game.ui.menus.Menu;
import main.java.com.game.ui.menus.MenuManager;
import main.java.com.game.ui.utils.RoundedRectangleButton;
import main.java.com.game.utils.Config;
import main.java.com.game.world.WorldConfig;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SingleplayerMenu extends Menu {

    private final int RECT_X = Config.BASE_WIDTH/4;
    private final int RECT_Y = Config.BASE_HEIGHT/5;
    private final int RECT_WIDTH = Config.BASE_WIDTH/2;
    private final int RECT_HEIGHT = Config.BASE_HEIGHT*8/15;

    private final int EXIT_BUTTON_X = Config.BASE_WIDTH/4;
    private final int EXIT_BUTTON_Y = Config.BASE_HEIGHT*12/15;
    private final int EXIT_BUTTON_WIDTH = Config.BASE_WIDTH/6;
    private final int EXIT_BUTTON_HEIGHT = Config.BASE_HEIGHT/15;

    private final int CREATE_BUTTON_X = Config.BASE_WIDTH*14/24;
    private final int CREATE_BUTTON_Y = Config.BASE_HEIGHT*12/15;
    private final int CREATE_BUTTON_WIDTH = Config.BASE_WIDTH/6;
    private final int CREATE_BUTTON_HEIGHT = Config.BASE_HEIGHT/15;

    private final Map<String, RoundedRectangleButton> buttons = new HashMap<>();
    private final WorldsChoiceManager worldsManager;



    public SingleplayerMenu() {
        super();
        worldsManager = new WorldsChoiceManager(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT, false);
        setButtons();
        
    }

    private void setButtons() {
        buttons.clear();

        int cornerRadius = 15;

        buttons.put("exitButton", new RoundedRectangleButton(EXIT_BUTTON_X, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT, cornerRadius, null, null, null));
        buttons.put("createButton", new RoundedRectangleButton(CREATE_BUTTON_X, CREATE_BUTTON_Y, CREATE_BUTTON_WIDTH, CREATE_BUTTON_HEIGHT, cornerRadius, null, null, null));

        setButtonsText();
    }

    private void setButtonsText() {
        buttons.get("exitButton").setText(LanguageManager.getText("menu.singleplayerMenu.buttons.done_button"), 16, Color.BLUE);
        buttons.get("createButton").setText(LanguageManager.getText("menu.singleplayerMenu.buttons.createWorld_button"), 16, Color.BLUE);
    }

    @Override
    public void update(int currentWidth, int currentHeight) {
        for (Map.Entry<String, RoundedRectangleButton> entry : buttons.entrySet()) {
            RoundedRectangleButton button = entry.getValue();
            if (button.update(currentWidth, currentHeight)) {
                switch (entry.getKey()) {
                    case "exitButton": {
                        MenuManager.setCurrentMenu("StartMenu");
                        break;
                    }
                    case "createButton": {
                        worldsManager.addWorld(new WorldConfig("defaultName", "123456"));
                        break;
                    }
                }
            }
        }

        worldsManager.update(currentWidth, currentHeight);

    }

    @Override
    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        for (RoundedRectangleButton button : buttons.values()) {
            button.draw(g, currentWidth, currentHeight);
        }
        worldsManager.draw(g, currentWidth, currentHeight);
    }


}


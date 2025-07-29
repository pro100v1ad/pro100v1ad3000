package main.java.com.pro100v1ad3000.ui.menus.multiplayer;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.systems.language.LanguageManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.ui.menus.Menus;
import main.java.com.pro100v1ad3000.ui.utils.InputFields;
import main.java.com.pro100v1ad3000.ui.utils.RoundedRectangleButton;
import main.java.com.pro100v1ad3000.utils.Config;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class MultiplayerMenu {

    private boolean isVisible;

    private final InputManager inputManager;
    private final AssetManager assetManager;
    private final LanguageManager languageManager;
    private final Menus menus;

    private Map<String, RoundedRectangleButton> buttons;
    private InputFields inputFields;


    public MultiplayerMenu(InputManager inputManager, AssetManager assetManager, LanguageManager languageManager, Menus menus) {

        this.inputManager = inputManager;
        this.assetManager = assetManager;
        this.languageManager = languageManager;
        this.menus = menus;

        buttons = new HashMap<>();
        inputFields = new InputFields(inputManager, assetManager, 100, 100, 60, 30);


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
        inputFields.update(currentWidth, currentHeight);
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        inputFields.draw(g);
    }

}

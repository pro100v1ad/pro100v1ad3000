package main.java.com.pro100v1ad3000.ui.menus.singleplayer.worldManager;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.ui.fonts.FontManager;
import main.java.com.pro100v1ad3000.ui.menus.Menu;
import main.java.com.pro100v1ad3000.utils.Config;
import main.java.com.pro100v1ad3000.world.WorldInfo;

import java.awt.*;

public class WorldList extends Menu {

    private int selectedIndex;
    private int scrollPosition;
    private final int posX, posY, width, height;

    public WorldList(int x, int y, int width, int height) {
        this.posX = x;
        this.posY = y;
        this.width = width;
        this.height = height;
        this.selectedIndex = -1;
        this.scrollPosition = 0;
    }

    @Override
    public void update(int currentWidth, int currentHeight) {

        int mouseX = InputManager.getMouseX();
        int mouseY = InputManager.getMouseY();

        int scaleMouseX = (int) (mouseX / (currentWidth / (float) Config.BASE_WIDTH));
        int scaleMouseY = (int) (mouseY / (currentHeight / (float) Config.BASE_HEIGHT));


    }

    @Override
    public void draw(Graphics2D g, int currentWidth, int currentHeight) {

        g.setColor(Color.WHITE);
        g.drawRect(posX, posY, width, height);
    }

}



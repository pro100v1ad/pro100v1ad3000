package main.java.com.pro100v1ad3000.ui.menus.singleplayer.worldManager;

import main.java.com.pro100v1ad3000.ui.menus.Menu;
import main.java.com.pro100v1ad3000.world.WorldInfo;

import java.awt.*;

public class WorldInfoPanel extends Menu {

    private final WorldInfo worldInfo;
    private int x, y, width, height;

    public WorldInfoPanel(int x, int y, int width, int height, String name, String seed) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        worldInfo = new WorldInfo(name, seed);
    }

    public WorldInfo getWorldInfo() {
        return worldInfo;
    }

    @Override
    public void update(int currentWidth, int currentHeight) {

    }

    @Override
    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        g.setColor(Color.WHITE);
        g.drawRect(x, y, width, height);
    }
}
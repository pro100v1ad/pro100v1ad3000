package main.java.com.pro100v1ad3000.ui.menus.singleplayer.worldManager;

import main.java.com.pro100v1ad3000.world.WorldInfo;
import java.awt.*;

public class WorldInfoPanel {
    private final int posX, posY, width, height;
    private final String name, seed;
    private boolean isVisible;

    public WorldInfoPanel(int posX, int posY, int width, int height, String name, String seed) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.name = name;
        this.seed = seed;
        this.isVisible = false;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public WorldInfo getWorldInfo() {
        return new WorldInfo(name, seed);
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        if (!isVisible) return;

        g.setColor(new Color(220, 220, 220, 200));
        g.fillRect(posX, posY, width, height);

        g.setColor(Color.BLACK);
        g.drawString("World Name: " + name, posX + 10, posY + 30);
        g.drawString("Seed: " + seed, posX + 10, posY + 60);
    }
}

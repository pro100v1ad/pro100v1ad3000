package main.java.com.pro100v1ad3000.ui.menus;

import java.awt.*;

public abstract class Menu {
    protected boolean isVisible;

    public Menu() {
        this.isVisible = false;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public abstract void update(int currentWidth, int currentHeight);

    public abstract void draw(Graphics2D g, int currentWidth, int currentHeight);
}


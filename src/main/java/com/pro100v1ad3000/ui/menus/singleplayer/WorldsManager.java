package main.java.com.pro100v1ad3000.ui.menus.singleplayer;


import main.java.com.pro100v1ad3000.ui.menus.Menu;
import main.java.com.pro100v1ad3000.ui.menus.singleplayer.worldManager.WorldInfoPanel;
import main.java.com.pro100v1ad3000.ui.menus.singleplayer.worldManager.WorldList;
import main.java.com.pro100v1ad3000.world.WorldInfo;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WorldsManager extends Menu {

    private final Map<String, WorldInfoPanel> worlds = new HashMap<>();
    private final int posWorldInfoPanelX, posWorldInfoPanelY, worldInfoPanelWidth, worldInfoPanelHeight;


    private final WorldList worldList;

    public WorldsManager(int posX, int posY, int width, int height) {
        this.posWorldInfoPanelX = posX + width;
        this.posWorldInfoPanelY = posY;
        this.worldInfoPanelWidth = width*2/3;
        this.worldInfoPanelHeight = height;

        worldList = new WorldList(posX, posY, width/3, height);
        addWorld("world1", "123456");
        setVisibleWorld(worlds.get("world1").getWorldInfo());
    }

    private void addWorld(String name, String seed) {
        worlds.put(name, new WorldInfoPanel(posWorldInfoPanelX, posWorldInfoPanelY, worldInfoPanelWidth, worldInfoPanelHeight, name, seed));
    }

    private void setVisibleWorld(WorldInfo world) {
        for(WorldInfoPanel worldInfoPanel: worlds.values()) {
            worldInfoPanel.setVisible(worldInfoPanel.getWorldInfo().equals(world));
        }
    }

    @Override
    public void update(int currentWidth, int currentHeight) {
        worldList.update(currentWidth, currentHeight);
        for(WorldInfoPanel worldInfoPanel: worlds.values()) {
            if(worldInfoPanel.isVisible()) {
                worldInfoPanel.update(currentWidth, currentHeight);
            }
        }
    }

    @Override
    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        worldList.draw(g, currentWidth, currentHeight);
        for(WorldInfoPanel worldInfoPanel: worlds.values()) {
            if(worldInfoPanel.isVisible()) {
                worldInfoPanel.draw(g, currentWidth, currentHeight);
            }
        }
    }


}

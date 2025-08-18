package main.java.com.pro100v1ad3000.ui.menus.singleplayer;

import main.java.com.pro100v1ad3000.ui.menus.Menu;
import main.java.com.pro100v1ad3000.ui.menus.singleplayer.worldManager.WorldInfoPanel;
import main.java.com.pro100v1ad3000.ui.menus.singleplayer.worldManager.WorldList;
import main.java.com.pro100v1ad3000.world.WorldInfo;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WorldsManager extends Menu {
    private final Map<String, WorldInfoPanel> worldInfoPanels = new HashMap<>();
    private final int posWorldInfoPanelX, posWorldInfoPanelY, worldInfoPanelWidth, worldInfoPanelHeight;
    private final WorldList worldList;

    public WorldsManager(int posX, int posY, int width, int height) {
        this.posWorldInfoPanelX = posX + width / 3;
        this.posWorldInfoPanelY = posY;
        this.worldInfoPanelWidth = width * 2 / 3;
        this.worldInfoPanelHeight = height;

        worldList = new WorldList(posX, posY, width / 3, height);


        // Добавляем тестовые миры
        addWorld(new WorldInfo("World 1", "123456"));
        addWorld(new WorldInfo("World 2", "654321"));
        addWorld(new WorldInfo("World 3", "112233"));
        addWorld(new WorldInfo("World 4", "112233"));
        addWorld(new WorldInfo("World 5", "112233"));
        addWorld(new WorldInfo("World 6", "112233"));
        addWorld(new WorldInfo("World 7", "112233"));
        addWorld(new WorldInfo("World 8", "112233"));
        addWorld(new WorldInfo("World 9", "112233"));
    }

    private void addWorld(WorldInfo world) {
        worldList.addWorld(world);
        worldInfoPanels.put(world.getName(), new WorldInfoPanel(
                posWorldInfoPanelX,
                posWorldInfoPanelY,
                worldInfoPanelWidth,
                worldInfoPanelHeight,
                world.getName(),
                world.getSeed()
        ));

    }

    @Override
    public void update(int currentWidth, int currentHeight) {
        worldList.update(currentWidth, currentHeight);
        WorldInfo selectedWorld = worldList.getSelectedWorld();
        if (selectedWorld != null) {
            setVisibleWorld(selectedWorld);
        }
    }

    private void setVisibleWorld(WorldInfo world) {
        for (WorldInfoPanel panel : worldInfoPanels.values()) {
            panel.setVisible(panel.getWorldInfo().getName().equals(world.getName()));
        }
    }

    @Override
    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        worldList.draw(g, currentWidth, currentHeight);
        for (WorldInfoPanel panel : worldInfoPanels.values()) {
            if (panel.isVisible()) {
                panel.draw(g, currentWidth, currentHeight);
            }
        }
    }
}

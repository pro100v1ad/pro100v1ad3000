package main.java.com.pro100v1ad3000.ui.menus.singleplayer;

import main.java.com.pro100v1ad3000.core.GamePlayStateManager;
import main.java.com.pro100v1ad3000.core.GameStateManager;
import main.java.com.pro100v1ad3000.ui.menus.singleplayer.worldManager.WorldInfoPanel;
import main.java.com.pro100v1ad3000.ui.menus.singleplayer.worldManager.WorldList;
import main.java.com.pro100v1ad3000.world.WorldConfig;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WorldsChoiceManager {
    private final Map<String, WorldInfoPanel> worldInfoPanels = new HashMap<>();
    private final int posWorldInfoPanelX, posWorldInfoPanelY, worldInfoPanelWidth, worldInfoPanelHeight;
    private final WorldList worldList;
    private final boolean isMultiplayer;

    private WorldInfoPanel panelToRemove; //  Костыль для удаления миров в многопоточности.

    public WorldsChoiceManager(int posX, int posY, int width, int height, boolean isMultiplayer) {
        this.posWorldInfoPanelX = posX + width / 3;
        this.posWorldInfoPanelY = posY;
        this.worldInfoPanelWidth = width * 2 / 3;
        this.worldInfoPanelHeight = height;
        this.isMultiplayer = isMultiplayer;

        worldList = new WorldList(posX, posY, width / 3, height);


        // Добавляем тестовые миры

    }

    public void addWorld(WorldConfig world) {
        worldList.addWorld(world);
        worldInfoPanels.put(world.getName(), new WorldInfoPanel(
                posWorldInfoPanelX,
                posWorldInfoPanelY,
                worldInfoPanelWidth,
                worldInfoPanelHeight,
                world,
                this
        ));

    }

    public void deleteWorld(WorldConfig world) {
       panelToRemove = new WorldInfoPanel(0, 0, 0, 0, world, this);
    }

    public void startGame(WorldConfig worldConfig) {
        GamePlayStateManager gamePlayStateManager = new GamePlayStateManager(worldConfig);
        GameStateManager.setGamePlayStateManager(gamePlayStateManager);
        GameStateManager.setActiveGame(true);
    }

    public void update(int currentWidth, int currentHeight) {
        worldList.update(currentWidth, currentHeight);
        WorldConfig selectedWorld = worldList.getSelectedWorld();
        if (selectedWorld != null) {
            setVisibleWorld(selectedWorld);
        }

        for (WorldInfoPanel panel : worldInfoPanels.values()) {
            if (panel.isVisible()) {
                panel.update(currentWidth, currentHeight);
            }
        }

        if(panelToRemove != null) {
            worldList.deleteWorld(panelToRemove.getWorldConfig());
            worldInfoPanels.remove(panelToRemove.getWorldConfig().getName());
            panelToRemove = null;
        }

    }

    private void setVisibleWorld(WorldConfig world) {
        for (WorldInfoPanel panel : worldInfoPanels.values()) {
            panel.setVisible(panel.getWorldConfig().getName().equals(world.getName()));
        }
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        worldList.draw(g, currentWidth, currentHeight);
        for (WorldInfoPanel panel : worldInfoPanels.values()) {
            if (panel.isVisible()) {
                panel.draw(g, currentWidth, currentHeight);
            }
        }
    }
}

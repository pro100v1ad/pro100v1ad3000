package main.java.com.game.ui.menus.singleplayer.worldManager;

import main.java.com.game.systems.InputManager;
import main.java.com.game.ui.utils.ScrollableArea;
import main.java.com.game.world.WorldConfig;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WorldList {
    private final int posX, posY, width, height;
    private final List<WorldConfig> worlds;
    private int selectedWorldIndex;
    private ScrollableArea scrollableArea;

    private final int worldRectHeight = 50;

    public WorldList(int posX, int posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.worlds = new ArrayList<>();
        this.selectedWorldIndex = -1;

        scrollableArea = new ScrollableArea(posX, posY, width, height, width, height);
    }

    public void addWorld(WorldConfig world) {
        worlds.add(world);
        scrollableArea.setVisibleArea(width, getTotalWorldsHeight());
    }

    public void deleteWorld(WorldConfig world) {
        worlds.remove(world);
        scrollableArea.setVisibleArea(width, getTotalWorldsHeight());
    }

    public void update(int currentWidth, int currentHeight) {
        int mouseX = InputManager.getMouseX();
        int mouseY = InputManager.getMouseY();

        // Проверяем наведение на миры
        for (int i = 0; i < worlds.size(); i++) {
            int worldY = posY + i * 50 - scrollableArea.getOffsetY();
            if (mouseX >= posX && mouseX <= posX + width &&
                    mouseY >= worldY && mouseY <= worldY + 40) {
                if (InputManager.isMouseButtonPressed(1) && mouseY < height + posY && mouseX < width + posX) {
                    selectedWorldIndex = i;
                }
                break;
            }
        }

        scrollableArea.update();
    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {
        // Сохраняем текущий clip
        Shape oldClip = g.getClip();

        // Устанавливаем новый clip только для области списка миров
        g.setClip(posX, posY, width, height);

        for (int i = 0; i < worlds.size(); i++) {
            int worldY = posY + i * worldRectHeight - scrollableArea.getOffsetY();

            // Проверяем, находится ли мир в видимой области (оптимизация)
            if (worldY + worldRectHeight >= posY && worldY <= posY + height) {
                // Отрисовка фона мира
                if (i == selectedWorldIndex) {
                    g.setColor(new Color(100, 150, 255, 150));
                } else if (InputManager.getMouseX() >= posX && InputManager.getMouseX() <= posX + width &&
                        InputManager.getMouseY() >= worldY && InputManager.getMouseY() <= worldY + 40) {
                    g.setColor(new Color(200, 200, 200, 150));
                } else {
                    g.setColor(new Color(255, 255, 255, 100));
                }
                g.fillRect(posX, worldY, width, 40);

                // Отрисовка текста с информацией о мире
                g.setColor(Color.BLACK);
                g.drawString(worlds.get(i).getName(), posX + 10, worldY + 25);
            }
        }

        // Восстанавливаем старый clip
        g.setClip(oldClip);

        scrollableArea.draw(g);
    }

    public int getTotalWorldsHeight() {
        return worlds.size() * worldRectHeight; // 50px на каждый мир
    }

    public WorldConfig getSelectedWorld() {
        if (selectedWorldIndex >= 0 && selectedWorldIndex < worlds.size()) {
            return worlds.get(selectedWorldIndex);
        }
        return null;
    }
}

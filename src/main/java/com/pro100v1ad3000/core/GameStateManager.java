package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.ui.menus.Menus;
import main.java.com.pro100v1ad3000.utils.Config;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GameStateManager {

    private boolean isActiveGame;

    private GamePlayStateManager gamePlayStateManager;
    private Menus menus;

    private InputManager inputManager;
    private AssetManager assetManager;

    public GameStateManager(InputManager inputManager, AssetManager assetManager) {
        this.inputManager = inputManager;
        this.assetManager = assetManager;
        gamePlayStateManager = new GamePlayStateManager(inputManager, assetManager);
        menus = new Menus(inputManager, assetManager);
    }

    public void update(float deltaTime) {
        // Обновляет состояние игры на основе времени, прошедшего с последнего кадра
        if(!isActiveGame) { // Режим меню
            menus.update();
        } else { // Активная игра
            gamePlayStateManager.update();
        }

    }

    public void render(Graphics2D g, int currentWidth, int currentHeight) {
        // Очистка экрана
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, currentWidth, currentHeight);

        // рассчитываем масштаб
        float scaleX = currentWidth / (float) Config.BASE_WIDTH;
        float scaleY = currentHeight / (float)Config.BASE_HEIGHT;


        AffineTransform originalTransform = g.getTransform(); // Сохраняем оригинальные трансформации
        g.scale(scaleX, scaleY); // Применяем масштабирование
        draw(g, currentWidth, currentHeight);// Отрисовка игровых объектов (в координатах 800х600)
        g.setTransform(originalTransform);// Восстанавливаем оригинальные трансформации

    }

    private void draw(Graphics2D g, int currentWidth, int currentHeight) {
        if(!isActiveGame) { // Режим меню
            menus.draw(g, currentWidth, currentHeight);
        } else { // Активная игра
            gamePlayStateManager.draw(g, currentWidth, currentHeight);
        }

    }




}

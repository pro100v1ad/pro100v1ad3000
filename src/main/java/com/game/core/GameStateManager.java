package main.java.com.game.core;

import main.java.com.game.ui.menus.MenuManager;
import main.java.com.game.utils.Config;
import main.java.com.game.utils.Logger;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GameStateManager {

    private static boolean isActiveGame;

    private static GamePlayStateManager gamePlayStateManager;
    private final MenuManager menuManager;



    public GameStateManager() {
        menuManager = new MenuManager();
    }

    public static void setGamePlayStateManager(GamePlayStateManager game) {
        gamePlayStateManager = game;
    }

    public static void setActiveGame(boolean activeGame) {
        isActiveGame = activeGame;
    }

    public void update(float deltaTime, int currentWidth, int currentHeight) {
        // Обновляет состояние игры на основе времени, прошедшего с последнего кадра
        if(!isActiveGame) { // Режим меню
            menuManager.update(currentWidth, currentHeight);
        } else { // Активная игра
            if(gamePlayStateManager == null) Logger.error("GamePlayStateManager is null");
            else gamePlayStateManager.update();
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
            menuManager.draw(g, currentWidth, currentHeight);
        } else { // Активная игра
            if(gamePlayStateManager == null) Logger.error("GamePlayStateManager is null");
            else gamePlayStateManager.draw(g, currentWidth, currentHeight);
        }

    }

}

package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.entities.players.utils.PlayerSettingsManager;
import main.java.com.pro100v1ad3000.utils.Logger;

import javax.swing.*;

public class GameMain {

    private GameWindow gameWindow;
    private GameLoop gameLoop;
    private GameStateManager gameStateManager;

    private PlayerSettingsManager playerSettingsManager;

    public void start() {

        SwingUtilities.invokeLater(() -> {
            // Инициализация компонентов

            gameWindow = new GameWindow();
            GamePanel gamePanel = gameWindow.getGamePanel();
            playerSettingsManager = new PlayerSettingsManager(); // Тоже сделать статическим, тк как игрок всего 1 для игры.
            gameStateManager = new GameStateManager();

            // Создаем игровой цикл
            gameLoop = new GameLoop(gameStateManager, gamePanel);

            //Запускаем
            gameWindow.setVisible(true);
            gameLoop.start();
        });

    }

    public void stop() {

        Logger.info("Завершение работы приложения");

        if(gameLoop != null) {
            gameLoop.stop();
        }
//        if(gamePlayStateManager != null) {
//            gamePlayStateManager.dispose();
//        }
        if(playerSettingsManager != null) {
            playerSettingsManager.dispose();
        }

        Logger.shutdown();
    }

    public static void main(String[] args) {

        Logger.initialize(Logger.LogLevel.DEBUG, "game.log");

        GameMain game = new GameMain();
        game.start();

        Runtime.getRuntime().addShutdownHook(new Thread(game::stop)); // Для корректного завершения
    }

}

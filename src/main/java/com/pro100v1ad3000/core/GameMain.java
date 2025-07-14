package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.utils.Logger;

public class GameMain {

    private GameLoop gameLoop;
    private GameStateManager gameStateManager;

    public void start() {
        gameStateManager = new GameStateManager();
        gameLoop = new GameLoop(gameStateManager);
        gameLoop.start();

    }

    public void stop() {

        Logger.info("Завершение работы приложения");
        Logger.shutdown();

        if(gameLoop != null) {
            gameLoop.stop();
        }
        if(gameStateManager != null) {
            gameStateManager.dispose();
        }
    }

    public static void main(String[] args) {

        Logger.initialize(Logger.LogLevel.DEBUG, "game.log");

        GameMain game = new GameMain();
        game.start();

        Runtime.getRuntime().addShutdownHook(new Thread(game::stop)); // Для корректного завершения
    }

}

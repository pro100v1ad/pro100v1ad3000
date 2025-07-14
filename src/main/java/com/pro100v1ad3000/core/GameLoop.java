package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.utils.Logger;

public class GameLoop {

    private final GameStateManager stateManager;
    private Thread renderThread;
    private Thread updateThread;
    private volatile boolean running;

    private static final int TARGET_FPS = 60;
    private static final int TARGET_UPS = 20;

    public GameLoop(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void start() {
        running = true;

        stateManager.initMultiplayer(false, "localhost");


        renderThread = new Thread(this::renderLoop, "Render-Thread");
        renderThread.setPriority(Thread.NORM_PRIORITY);

        updateThread = new Thread(this::updateLoop, "Update-Thread");
        updateThread.setPriority(Thread.MAX_PRIORITY);

        renderThread.start();
        updateThread.start();
    }

    public void stop() {
        running = false;

        try {
            if (renderThread != null) renderThread.join();
            if (updateThread != null) updateThread.join();
        } catch (InterruptedException e) {
            Logger.error("Error stopping game threads: " + e.getMessage());
            Thread.currentThread().interrupt();

        }

    }

    private void renderLoop() {

        // Реализовать логику 60 FPS
        while (running) {
            // Оставляем немного времени для других потоков
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void updateLoop() {
        // Реализовать логику 20 UPS
        while (running) {
            // Оставляем немного времени для других потоков
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void render() {

    }

    private void update() {

    }
}

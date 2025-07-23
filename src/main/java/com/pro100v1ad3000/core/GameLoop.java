package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameLoop {

    private final GameStateManager gameStateManager;
    private final GamePanel gamePanel;
    private Thread renderThread;
    private Thread updateThread;
    private volatile boolean running;

    private static final int TARGET_FPS = 60;
    private static final int TARGET_UPS = 20;
    private static final long NANOS_PER_SECOND = 1_000_000_000;

    public GameLoop(GameStateManager gameStateManager, GamePanel gamePanel) {
        this.gameStateManager = gameStateManager;
        this.gamePanel = gamePanel;
    }

    public void start() {
        running = true;

        // Поток рендеринга (60 FPS)
        renderThread = new Thread(this::renderLoop, "Render-Thread");
        renderThread.setPriority(Thread.NORM_PRIORITY);

        // Поток рендеринга (20 UPS)
        updateThread = new Thread(this::updateLoop, "Update-Thread");
        updateThread.setPriority(Thread.MAX_PRIORITY);

        renderThread.start();
        updateThread.start();
    }

    public void stop() {
        running = false;

        try {
            if (renderThread != null) {
                renderThread.interrupt();
                renderThread.join(100);
            }
            if (updateThread != null) {
                updateThread.interrupt();
                updateThread.join(100);
            }
        } catch (InterruptedException e) {
            Logger.error("Error stopping game threads: " + e.getMessage());
            Thread.currentThread().interrupt();

        }

    }

    private void renderLoop() {
        final double frameTime = NANOS_PER_SECOND / TARGET_FPS;
        long lastTime = System.nanoTime();
        double unprocessed = 0;

        while (running) {

            long now = System.nanoTime();
            unprocessed += (now - lastTime) / frameTime;
            lastTime = now;

            while (unprocessed >= 1) {
                render();
                unprocessed--;
            }

            // Оставляем немного времени для других потоков
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void updateLoop() {

        final double updateTime = NANOS_PER_SECOND / TARGET_UPS;
        long lastTime = System.nanoTime();
        double unprocessed = 0;

        while (running) {

            long now = System.nanoTime();
            unprocessed += (now - lastTime) / updateTime;
            lastTime = now;

            while (unprocessed >= 1) {
                update(1.0f / TARGET_UPS);
                unprocessed--;
            }

            // Оставляем немного времени для других потоков
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    private void render() {

        Dimension currentSize = gamePanel.getCurrentSize();

        // Создаем буфер текущего размера
        BufferedImage buffer = new BufferedImage(
                currentSize.width,
                currentSize.height,
                BufferedImage.TYPE_INT_RGB
        );

        // Рендерим игровой мир
        Graphics2D g = buffer.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        gameStateManager.render(g, currentSize.width, currentSize.height);
        g.dispose();

        // Обновляем панель
        gamePanel.updateBuffer(buffer);
        SwingUtilities.invokeLater(gamePanel::repaint);

    }

    private void update(float deltaTime) {
        Dimension currentSize = gamePanel.getCurrentSize();
        gameStateManager.update(deltaTime, currentSize.width, currentSize.height);
    }
}

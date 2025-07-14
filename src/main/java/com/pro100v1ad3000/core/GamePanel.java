package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.utils.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private BufferedImage renderBuffer;
    private final Object bufferLock = new Object();

    private InputManager inputManager;

    public GamePanel() {
        setPreferredSize(new Dimension(Config.BASE_WIDTH, Config.BASE_HEIGHT));
        setDoubleBuffered(false); // Используем свой буфер
        inputManager = new InputManager(this);
        setFocusable(true);
    }

    public void onResize() {
        repaint();
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void updateBuffer(BufferedImage newBuffer) {
        synchronized (bufferLock) {
            this.renderBuffer = newBuffer;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        synchronized (bufferLock) {
            if(renderBuffer != null) {

                // Растягиваем изображение на всю доступную область
                g.drawImage(renderBuffer, 0, 0, getWidth(), getHeight(), null);
            }
        }
    }

    public Dimension getCurrentSize() {
        return new Dimension(getWidth(), getHeight());
    }

}

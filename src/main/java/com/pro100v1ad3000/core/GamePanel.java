package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.utils.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private BufferedImage renderBuffer;
    private final Object bufferLock = new Object();

    public GamePanel() {
        setPreferredSize(new Dimension(Config.BASE_WINDOW_WIDTH, Config.BASE_WINDOW_HEIGHT));
        setDoubleBuffered(false); // Используем свой буфер
        setFocusable(true);
        InputManager.initialize(this);
    }

    public void onResize() {
        repaint();
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

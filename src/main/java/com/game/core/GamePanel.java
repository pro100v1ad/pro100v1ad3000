package main.java.com.game.core;

import main.java.com.game.systems.InputManager;
import main.java.com.game.utils.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private static GamePanel instance;

    private BufferedImage renderBuffer;
    private final Object bufferLock = new Object();

    public GamePanel() {

        instance = this;

        setPreferredSize(new Dimension(Config.BASE_WINDOW_WIDTH, Config.BASE_WINDOW_HEIGHT));
        setDoubleBuffered(false); // Используем свой буфер
        setFocusable(true);
        InputManager.initialize(this);
    }

    public static GamePanel getInstance(){
        return instance;
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

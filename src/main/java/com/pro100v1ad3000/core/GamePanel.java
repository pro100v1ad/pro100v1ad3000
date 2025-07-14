package main.java.com.pro100v1ad3000.core;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {

    private BufferedImage renderBuffer;
    private final Object bufferLock = new Object();

    public GamePanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setDoubleBuffered(false); // Используем свой буфер
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
                g.drawImage(renderBuffer, 0, 0, null);
            }
        }
    }



}

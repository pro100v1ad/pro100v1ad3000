package main.java.com.pro100v1ad3000.core;

import javax.swing.*;

public class GameWindow extends JFrame {

    private final GamePanel gamePanel;

    public GameWindow() {

        setTitle("pro100v1ad3000 games");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel(800, 600);
        getContentPane().add(gamePanel);
        pack();
        setLocationRelativeTo(null);

    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

}

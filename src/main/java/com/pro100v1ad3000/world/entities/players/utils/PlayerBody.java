package main.java.com.pro100v1ad3000.world.entities.players.utils;

import java.awt.*;

public class PlayerBody {

    // Константы для размеров частей тела
    private static final int HEAD_SIZE = 50;
    private static final int BODY_WIDTH = 70;
    private static final int BODY_HEIGHT = 100;
    private static final int ARM_WIDTH = 20;
    private static final int ARM_HEIGHT = 60;
    private static final int LEG_WIDTH = 20;
    private static final int LEG_HEIGHT = 80;

    // Параметры для позиции персонажа
    private int playerX;
    private int playerY;

    // Цвета частей тела
    private Color headColor;
    private Color bodyColor;
    private Color armColor;
    private Color legColor;

    public PlayerBody(int playerX, int playerY) {
        this.playerX = playerX;
        this.playerY = playerY;

        // Устанавливаем цвета по умолчанию
        headColor = Color.PINK;
        bodyColor = Color.BLUE;
        armColor = Color.GREEN;
        legColor = Color.ORANGE;
    }

    public void update(int currentWidth, int currentHeight) {
        // Здесь можно обновить состояние персонажа, если это необходимо
    }

    public void draw(Graphics2D g) {
        // Сохраняем текущие настройки графики
        Color oldColor = g.getColor();

        // Рисуем голову
        g.setColor(headColor);
        g.fillOval(playerX - HEAD_SIZE / 2, playerY - HEAD_SIZE - BODY_HEIGHT / 2, HEAD_SIZE, HEAD_SIZE);

        // Рисуем тело
        g.setColor(bodyColor);
        g.fillRect(playerX - BODY_WIDTH / 2, playerY - BODY_HEIGHT / 2, BODY_WIDTH, BODY_HEIGHT);

        // Рисуем левую руку
        g.setColor(armColor);
        g.fillRect(playerX - BODY_WIDTH / 2 - ARM_WIDTH, playerY - ARM_HEIGHT / 2, ARM_WIDTH, ARM_HEIGHT);

        // Рисуем правую руку
        g.fillRect(playerX + BODY_WIDTH / 2, playerY - ARM_HEIGHT / 2, ARM_WIDTH, ARM_HEIGHT);

        // Рисуем левую ногу
        g.setColor(legColor);
        g.fillRect(playerX - LEG_WIDTH / 2 - BODY_WIDTH / 4, playerY + BODY_HEIGHT / 2, LEG_WIDTH, LEG_HEIGHT);

        // Рисуем правую ногу
        g.fillRect(playerX + LEG_WIDTH / 2 + BODY_WIDTH / 4, playerY + BODY_HEIGHT / 2, LEG_WIDTH, LEG_HEIGHT);

        // Восстанавливаем старые настройки графики
        g.setColor(oldColor);
    }

    // Методы для изменения цвета частей тела
    public void setHeadColor(Color color) {
        headColor = color;
    }

    public void setBodyColor(Color color) {
        bodyColor = color;
    }

    public void setArmColor(Color color) {
        armColor = color;
    }

    public void setLegColor(Color color) {
        legColor = color;
    }

    // Методы для изменения позиции персонажа
    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }
}

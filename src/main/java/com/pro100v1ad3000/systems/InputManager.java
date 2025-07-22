package main.java.com.pro100v1ad3000.systems;

import main.java.com.pro100v1ad3000.core.GamePanel;

import java.awt.event.*;
import java.util.concurrent.ConcurrentHashMap;

public class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private final ConcurrentHashMap<Integer, Boolean> keyStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Boolean> mouseButtonStates = new ConcurrentHashMap<>();
    private int mouseX, mouseY;
    private int mouseScroll;
    private boolean isMouseWindow;

    public InputManager(GamePanel gamePanel) {
        gamePanel.addKeyListener(this);
        gamePanel.addMouseListener(this);
        gamePanel.addMouseMotionListener(this);
        gamePanel.addMouseWheelListener(this);
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    // Метод для сброса состояния всех клавиш и кнопок мыши
    public void resetInputStates() {
        // Сброс состояния клавиш
        for (Integer keyCode : keyStates.keySet()) {
            keyStates.put(keyCode, false);
        }

        // Сброс состояния кнопок мыши
        for (Integer button : mouseButtonStates.keySet()) {
            mouseButtonStates.put(button, false);
        }
    }

    // Проверка состояния клавиш
    public boolean isKeyPressed(int keyCode) {
        return keyStates.getOrDefault(keyCode, false);
    }

    public boolean isMouseButtonPressed(int button) {
        return mouseButtonStates.getOrDefault(button, false);
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getMouseScroll() {
        return mouseScroll;
    }

    public boolean isMouseWindow() {
        return isMouseWindow;
    }

    // Методы для обновления состояния (вызываются в конце кадра)
    public void endFrame() {
        mouseScroll = 0;
    }

    // Реализация интерфейсов обработки событий
    @Override
    public void keyPressed(KeyEvent e) {
        keyStates.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyStates.put(e.getKeyCode(), false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseButtonStates.put(e.getButton(), true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseButtonStates.put(e.getButton(), false);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseScroll += e.getWheelRotation();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isMouseWindow = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isMouseWindow = false;
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}

}

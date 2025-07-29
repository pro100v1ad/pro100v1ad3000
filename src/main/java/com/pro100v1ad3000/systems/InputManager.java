package main.java.com.pro100v1ad3000.systems;

import main.java.com.pro100v1ad3000.core.GamePanel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InputManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private final ConcurrentHashMap<Integer, Boolean> keyStates = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Boolean> mouseButtonStates = new ConcurrentHashMap<>();
    private StringBuilder typedChars = new StringBuilder();
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

    public void resetInputStates() {
        for (Integer keyCode : keyStates.keySet()) {
            // Не сбрасываем состояние служебных клавиш, таких как Shift
            if (!isServiceKey(keyCode)) {
                keyStates.put(keyCode, false);
            }
        }
        for (Integer button : mouseButtonStates.keySet()) {
            mouseButtonStates.put(button, false);
        }
    }

    // Вспомогательный метод для проверки, является ли клавиша служебной
    private boolean isServiceKey(int keyCode) {
        return keyCode == KeyEvent.VK_SHIFT ||
                keyCode == KeyEvent.VK_CONTROL ||
                keyCode == KeyEvent.VK_ALT ||
                keyCode == KeyEvent.VK_CAPS_LOCK;
    }


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

    public void endFrame() {
        mouseScroll = 0;
    }

    public List<Integer> getPressedKeyCodes() {
        List<Integer> pressedKeyCodes = new ArrayList<>();
        for (Integer keyCode : keyStates.keySet()) {
            if (keyStates.get(keyCode)) {
                pressedKeyCodes.add(keyCode);
            }
        }
        return pressedKeyCodes;
    }

    public char[] getTypedChars() {
        char[] chars = typedChars.toString().toCharArray();
        typedChars.setLength(0);
        return chars;
    }

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

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        if (c != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(c)) {
            typedChars.append(c);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}

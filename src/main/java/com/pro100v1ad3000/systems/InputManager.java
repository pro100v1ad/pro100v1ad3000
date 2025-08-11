package main.java.com.pro100v1ad3000.systems;

import main.java.com.pro100v1ad3000.core.GamePanel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InputManager {
    private static final ConcurrentHashMap<Integer, Boolean> keyStates = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Boolean> mouseButtonStates = new ConcurrentHashMap<>();
    private static final StringBuilder typedChars = new StringBuilder();
    private static int mouseX, mouseY;
    private static int mouseScroll;
    private static boolean isMouseWindow;

    // Приватный конструктор, чтобы предотвратить создание экземпляров
    private InputManager() {}

    // Инициализация InputManager с GamePanel
    public static void initialize(GamePanel gamePanel) {
        gamePanel.addKeyListener(new KeyHandler());
        gamePanel.addMouseListener(new MouseHandler());
        gamePanel.addMouseMotionListener(new MouseHandler());
        gamePanel.addMouseWheelListener(new MouseWheelHandler());
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    public static void resetInputStates() {
        for (Integer keyCode : keyStates.keySet()) {
            if (!isServiceKey(keyCode)) {
                keyStates.put(keyCode, false);
            }
        }
        for (Integer button : mouseButtonStates.keySet()) {
            mouseButtonStates.put(button, false);
        }
    }

    private static boolean isServiceKey(int keyCode) {
        return keyCode == KeyEvent.VK_SHIFT ||
                keyCode == KeyEvent.VK_CONTROL ||
                keyCode == KeyEvent.VK_ALT ||
                keyCode == KeyEvent.VK_CAPS_LOCK;
    }

    public static void resetKeyState(int keyCode) {
        keyStates.put(keyCode, false);
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyStates.getOrDefault(keyCode, false);
    }

    public static boolean isMouseButtonPressed(int button) {
        return mouseButtonStates.getOrDefault(button, false);
    }

    public static int getMouseX() {
        return mouseX;
    }

    public static int getMouseY() {
        return mouseY;
    }

    public static int getMouseScroll() {
        return mouseScroll;
    }

    public static boolean isMouseWindow() {
        return isMouseWindow;
    }

    public static void endFrame() {
        mouseScroll = 0;
    }

    public static List<Integer> getPressedKeyCodes() {
        List<Integer> pressedKeyCodes = new ArrayList<>();
        for (Integer keyCode : keyStates.keySet()) {
            if (keyStates.get(keyCode)) {
                pressedKeyCodes.add(keyCode);
            }
        }
        return pressedKeyCodes;
    }

    public static char[] getTypedChars() {
        char[] chars = typedChars.toString().toCharArray();
        typedChars.setLength(0);
        return chars;
    }

    // Внутренние классы для обработки событий
    private static class KeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            keyStates.put(e.getKeyCode(), true);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keyStates.put(e.getKeyCode(), false);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (c != KeyEvent.CHAR_UNDEFINED && !Character.isISOControl(c)) {
                typedChars.append(c);
            }
        }
    }

    private static class MouseHandler extends MouseAdapter {
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
        public void mouseEntered(MouseEvent e) {
            isMouseWindow = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            isMouseWindow = false;
        }
    }

    private static class MouseWheelHandler implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            mouseScroll += e.getWheelRotation();
        }
    }
}

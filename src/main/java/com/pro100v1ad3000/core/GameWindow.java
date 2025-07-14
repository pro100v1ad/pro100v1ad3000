package main.java.com.pro100v1ad3000.core;

import main.java.com.pro100v1ad3000.utils.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class GameWindow extends JFrame {

    private final GamePanel gamePanel;
    private boolean isFullscreen = false;
    private GraphicsDevice device;
    private final Dimension minSize = new Dimension(Config.MINIMUM_WIDTH, Config.MINIMUM_HEIGHT);
    private Dimension windowedSize = new Dimension(Config.BASE_WIDTH, Config.BASE_HEIGHT);


    public GameWindow() {

        setTitle("pro100v1ad3000 games");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Получаем графическое устройство
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Настройка панели
        gamePanel = new GamePanel();
        add(gamePanel);

        // Установка начального размера
        setMinimumSize(minSize);
        setSize(windowedSize);
        setLocationRelativeTo(null);

        //Настройка горячих клавиш
        setupKeyBindings();

    }

    private void setupKeyBindings() {
        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gamePanel.getActionMap();

        // Переключение полноэкранного режима по F11
        inputMap.put(KeyStroke.getKeyStroke("F11"), "toggleFullscreen");
        actionMap.put("toggleFullscreen", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFullscreen();
            }
        });

        // Выход по Alt+F4
        inputMap.put(KeyStroke.getKeyStroke("alt F4"), "exit");
        actionMap.put("exit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispatchEvent(new WindowEvent(GameWindow.this, WindowEvent.WINDOW_CLOSING));
            }
        });
    }

    private void toggleFullscreen() {
        if(isFullscreen) {
            exitFullscreen();
        } else {
            enterFullscreen();
        }
        isFullscreen = !isFullscreen;
        gamePanel.onResize();
    }

    public void enterFullscreen() {
        // Сохраняем текущие размеры окна перед переходом в полноэкранный режим
        windowedSize = getSize();

        // Сначала делаем окно невидимым для изменения свойств
        setVisible(false);

        dispose();

        // Убираем декорации окна
        setUndecorated(true);

        // Устанавливаем полноэкранный режим
        device.setFullScreenWindow(this);

        // Убедимся, что окно получило фокус
        requestFocus();

        // Показываем окно снова
        setVisible(true);
    }

    private void exitFullscreen() {
        // Выходим из полноэкранного режима
        device.setFullScreenWindow(null);

        // Делаем окно невидимым для изменения свойств
        setVisible(false);
        dispose();
        // Возвращаем декорации окна
        setUndecorated(false);

        // Восстанавливаем предыдущий размер
        setSize(windowedSize);
        setLocationRelativeTo(null);

        // Возвращаем фокус
        requestFocus();

        // Показываем окно
        setVisible(true);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

}

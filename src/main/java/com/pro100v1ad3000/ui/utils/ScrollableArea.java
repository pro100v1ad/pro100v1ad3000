package main.java.com.pro100v1ad3000.ui.utils;

import main.java.com.pro100v1ad3000.systems.InputManager;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ScrollableArea {
    // Позиция и размер видимой области
    private int posX, posY, visibleWidth, visibleHeight;
    // Реальный размер контента (который может быть больше видимой области)
    private int realWidth, realHeight;
    // Флаги отображения полос прокрутки
    private boolean showHorizontalScrollbar, showVerticalScrollbar;
    // Текущие смещения и максимальные возможные смещения
    private int offsetX, offsetY, maxOffsetX, maxOffsetY;
    // Толщина полос прокрутки
    private static final int SCROLLBAR_THICKNESS = 10;
    // Цвета полос прокрутки
    private static final Color SCROLLBAR_COLOR = new Color(150, 150, 150, 200);
    private static final Color SCROLLBAR_THUMB_COLOR = new Color(0, 100, 100, 220);

    public ScrollableArea(int posX, int posY, int visibleWidth, int visibleHeight, int realWidth, int realHeight) {
        this.posX = posX;
        this.posY = posY;
        this.visibleWidth = visibleWidth;
        this.visibleHeight = visibleHeight;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        this.offsetX = 0;
        this.offsetY = 0;
        updateLogic();
    }

    // Обновляет логику полос прокрутки при изменении размеров
    private void updateLogic() {
        showVerticalScrollbar = realHeight > visibleHeight;
        showHorizontalScrollbar = realWidth > visibleWidth;
        maxOffsetX = Math.max(0, realWidth - visibleWidth);
        maxOffsetY = Math.max(0, realHeight - visibleHeight);
    }

    // Исправляет смещения, чтобы они не выходили за границы
    private void fixOffsets() {
        offsetX = Math.max(0, Math.min(offsetX, maxOffsetX));
        offsetY = Math.max(0, Math.min(offsetY, maxOffsetY));
    }

    // Возвращает процент вертикальной прокрутки (0-100)
    public float getVerticalScrollingPercent() {
        return maxOffsetY > 0 ? (offsetY / (float) maxOffsetY) * 100f : 0f;
    }

    // Возвращает процент горизонтальной прокрутки (0-100)
    public float getHorizontalScrollingPercent() {
        return maxOffsetX > 0 ? (offsetX / (float) maxOffsetX) * 100f : 0f;
    }

    // Устанавливает новую видимую область
    public void setVisibleArea(int width, int height) {
        this.realWidth = width;
        this.realHeight = height;
        updateLogic();
        fixOffsets();
    }

    // Обновляет состояние прокрутки (обработка колесика мыши)
    public void update() {
        int scroll = InputManager.getMouseScroll();
        if (scroll != 0) {
            if (InputManager.isKeyPressed(KeyEvent.VK_SHIFT) && showHorizontalScrollbar) {
                // Горизонтальная прокрутка с зажатым Shift
                offsetX += scroll * 20;
                fixOffsets();
            } else if (showVerticalScrollbar) {
                // Вертикальная прокрутка по умолчанию
                offsetY += scroll * 20;
                fixOffsets();
            }
        }
        InputManager.endFrame();
    }

    // Отрисовывает полосы прокрутки
    public void drawScrollbars(Graphics2D g) {
        // Отрисовка вертикальной полосы прокрутки
        if (showVerticalScrollbar) {
            int scrollbarX = posX + visibleWidth - SCROLLBAR_THICKNESS;
            int scrollbarY = posY;
            int scrollbarHeight = visibleHeight;

            // Фон полосы
            g.setColor(SCROLLBAR_COLOR);
            g.fillRect(scrollbarX, scrollbarY, SCROLLBAR_THICKNESS, scrollbarHeight);

            // Ползунок
            float thumbHeight = (float) visibleHeight / realHeight * scrollbarHeight;
            float thumbY = (float) offsetY / maxOffsetY * (scrollbarHeight - thumbHeight);
            g.setColor(SCROLLBAR_THUMB_COLOR);
            g.fillRect(scrollbarX, scrollbarY + (int) thumbY, SCROLLBAR_THICKNESS, (int) thumbHeight);
        }

        // Отрисовка горизонтальной полосы прокрутки
        if (showHorizontalScrollbar) {
            int scrollbarX = posX;
            int scrollbarY = posY + visibleHeight - SCROLLBAR_THICKNESS;
            int scrollbarWidth = visibleWidth;

            // Фон полосы
            g.setColor(SCROLLBAR_COLOR);
            g.fillRect(scrollbarX, scrollbarY, scrollbarWidth, SCROLLBAR_THICKNESS);

            // Ползунок
            float thumbWidth = (float) visibleWidth / realWidth * scrollbarWidth;
            float thumbX = (float) offsetX / maxOffsetX * (scrollbarWidth - thumbWidth);
            g.setColor(SCROLLBAR_THUMB_COLOR);
            g.fillRect(scrollbarX + (int) thumbX, scrollbarY, (int) thumbWidth, SCROLLBAR_THICKNESS);
        }
    }

    // Основной метод отрисовки (можно расширить для отрисовки контента)
    public void draw(Graphics2D g) {
        drawScrollbars(g);
    }

    // Геттеры для текущих смещений (для использования в отрисовки контента)
    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }
}

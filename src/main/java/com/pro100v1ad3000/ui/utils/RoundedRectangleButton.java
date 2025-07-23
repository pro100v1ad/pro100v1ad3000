package main.java.com.pro100v1ad3000.ui.utils;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.ui.fonts.FontManager;
import main.java.com.pro100v1ad3000.utils.Config;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RoundedRectangleButton {

    private InputManager inputManager;

    private int rectX, rectY, rectWidth, rectHeight, cornerRadius;
    private BufferedImage defaultImage, activeImage, pressedButton;
    private boolean isDefaultButton, isActiveButton, isPressedButton;
    private String text;
    private float fontSize;
    private Color textColor;

    public RoundedRectangleButton(InputManager inputManager, int rectX, int rectY, int rectWidth, int rectHeight, int cornerRadius, String defaultImageName, String activeImageName, String pressedImageName) {
        this.rectX = rectX;
        this.rectY = rectY;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        this.cornerRadius = cornerRadius;
        loadImages(defaultImageName, activeImageName, pressedImageName);

        this.inputManager = inputManager;

    }

    private void drawButton(Graphics2D g, Color buttonColor, Color borderColor, float borderThickness) {
        // Сохраняем текущие настройки графики
        Stroke oldStroke = g.getStroke();
        Color oldColor = g.getColor();

        // Устанавливаем цвет и толщину линии для границы
        g.setColor(borderColor);
        g.setStroke(new BasicStroke(borderThickness));

        // Рисуем закругленный прямоугольник для границы
        g.drawRoundRect(rectX, rectY, rectWidth, rectHeight, cornerRadius, cornerRadius);

        // Устанавливаем цвет для заливки кнопки
        g.setColor(buttonColor);

        // Рисуем и заполняем закругленный прямоугольник для кнопки
        g.fillRoundRect(rectX, rectY, rectWidth, rectHeight, cornerRadius, cornerRadius);

        // Восстанавливаем старые настройки графики
        g.setStroke(oldStroke);
        g.setColor(oldColor);
    }

    private void loadImages(String defaultImageName, String activeImageName, String pressedImageName) {
        if(defaultImageName != null) {

        } else {

        }

        if(activeImageName != null) {

        } else {

        }

        if(pressedImageName != null) {

        } else {

        }
    }




    private boolean isPointInRoundedRectangle(int mouseX, int mouseY) {

        if(mouseX < rectX || mouseX > rectX + rectWidth || mouseY < rectY || mouseY > rectY + rectHeight) {
            return false;
        }

        // Левый верхний угол
        if ((mouseX - rectX) * (mouseX - rectX) + (mouseY - rectY) * (mouseY - rectY) > cornerRadius * cornerRadius) {
            // Правый верхний угол
            if ((mouseX - (rectX + rectWidth)) * (mouseX - (rectX + rectWidth)) + (mouseY - rectY) * (mouseY - rectY) > cornerRadius * cornerRadius) {
                // Левый нижний угол
                if ((mouseX - rectX) * (mouseX - rectX) + (mouseY - (rectY + rectHeight)) * (mouseY - (rectY + rectHeight)) > cornerRadius * cornerRadius) {
                    // Правый нижний угол
                    if ((mouseX - (rectX + rectWidth)) * (mouseX - (rectX + rectWidth)) + (mouseY - (rectY + rectHeight)) * (mouseY - (rectY + rectHeight)) > cornerRadius * cornerRadius) {
                        return true;
                    }
                }
            }
        }

        return true;

    }

    public void setText(String text, float fontSize, Color textColor) {
        this.text = text;
        this.fontSize = fontSize;
        this.textColor = textColor;
    }

    public int getRectWidth() {
        return rectWidth;
    }

    public int getRectHeight() {
        return rectHeight;
    }

    public boolean update(int currentWidth, int currentHeight) {

        int mouseX = inputManager.getMouseX();
        int mouseY = inputManager.getMouseY();

        int scaleMouseX = (int) (mouseX / (currentWidth / (float) Config.BASE_WIDTH));
        int scaleMouseY = (int) (mouseY / (currentHeight / (float) Config.BASE_HEIGHT));

        if(isPointInRoundedRectangle(scaleMouseX, scaleMouseY)) {
            isActiveButton = true;
            isDefaultButton = false;
        } else {
            isActiveButton = false;
            isDefaultButton = true;
        }

        isPressedButton = inputManager.isMouseButtonPressed(1) && isActiveButton;
        return isPressedButton;

    }

    public void draw(Graphics2D g, int currentWidth, int currentHeight) {

        if(isDefaultButton) g.drawImage(defaultImage, rectX, rectY, rectWidth, rectHeight, null);
        if(isActiveButton) g.drawImage(defaultImage, rectX, rectY, rectWidth, rectHeight, null);
        if(isPressedButton) g.drawImage(defaultImage, rectX, rectY, rectWidth, rectHeight, null);

        if(isDefaultButton)drawButton(g, Color.WHITE, Color.BLUE, 2.0f);
        if(isActiveButton)drawButton(g, Color.YELLOW, Color.BLUE, 2.0f);
        if(isPressedButton)drawButton(g, Color.GRAY, Color.BLUE, 2.0f);

        drawText(g);
    }

    private void drawText(Graphics2D g) {
        if (text != null && !text.isEmpty()) {
            Font font = FontManager.getFont("defaultFont");
            if (font != null) {
                // Устанавливаем шрифт с нужным размером
                font = font.deriveFont(fontSize).deriveFont(Font.BOLD);
                g.setFont(font);
            } else {
                // Используем шрифт по умолчанию, если не удалось загрузить кастомный шрифт
                g.setFont(new Font("Arial", Font.PLAIN, (int) fontSize).deriveFont(Font.BOLD));
            }

            // Получаем метрики шрифта для расчета позиции текста
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();

            // Рассчитываем координаты для центрирования текста
            int textX = rectX + (rectWidth - textWidth) / 2;
            int textY = rectY + (rectHeight - textHeight) / 2 + fm.getAscent();

            // Устанавливаем цвет текста
            g.setColor(textColor);

            // Рисуем текст
            g.drawString(text, textX, textY);
        }
    }

}

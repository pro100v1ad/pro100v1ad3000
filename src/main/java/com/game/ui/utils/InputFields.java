package main.java.com.game.ui.utils;

import main.java.com.game.systems.InputManager;


import java.awt.*;
import java.awt.event.KeyEvent;

public class InputFields {
    private TextArea textArea;
    private int posX, posY, width, height, cornerRadius;
    private boolean isDefaultImage, isActiveImage, isPressedImage;

    private boolean isActiveTextArea;

    public InputFields(int posX, int posY, int width, int height) {
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.textArea = new TextArea(posX, posY, width, height);
        this.isActiveTextArea = false;
        this.cornerRadius = 15;
    }

    private boolean isPointInRoundedRectangle(int mouseX, int mouseY) {


        if(mouseX < posX || mouseX > posX + width || mouseY < posY || mouseY > posY + height) {
            return false;
        }

        // Левый верхний угол
        if ((mouseX - posX) * (mouseX - posX) + (mouseY - posY) * (mouseY - posY) > cornerRadius * cornerRadius) {
            // Правый верхний угол
            if ((mouseX - (posX + width)) * (mouseX - (posX + width)) + (mouseY - posY) * (mouseY - posY) > cornerRadius * cornerRadius) {
                // Левый нижний угол
                if ((mouseX - posX) * (mouseX - posX) + (mouseY - (posY + height)) * (mouseY - (posY + height)) > cornerRadius * cornerRadius) {
                    // Правый нижний угол
                    if ((mouseX - (posX + width)) * (mouseX - (posX + width)) + (mouseY - (posY + height)) * (mouseY - (posY + height)) > cornerRadius * cornerRadius) {
                        return true;
                    }
                }
            }
        }

        return true;

    }

    public boolean isActiveTextArea() {
        return isActiveTextArea;
    }

    public String getText() {
        return textArea.getText();
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    public void update(int currentWidth, int currentHeight) {
        textArea.update();

        int mouseX = InputManager.getMouseX();
        int mouseY = InputManager.getMouseY();

        boolean flag = isPointInRoundedRectangle(mouseX, mouseY);
        if(flag && InputManager.isMouseButtonPressed(1)) {
            textArea.setActive(true);
            isActiveTextArea = true;
            isPressedImage = true;
            isActiveImage = false;
            isDefaultImage = false;
        } else if(textArea.isActive() && InputManager.isMouseButtonPressed(1) && !flag || InputManager.isKeyPressed(KeyEvent.VK_ENTER)) {
            textArea.setActive(false);
            isActiveTextArea = false;
            isPressedImage = false;
            isActiveImage = false;
            isDefaultImage = false;
        } else if(flag) {
            isActiveImage = true;
            isDefaultImage = false;
        } else {
            isDefaultImage = true;
            isActiveImage = false;
        }
        InputManager.resetInputStates();
    }

    public void draw(Graphics2D g) {
        // TEMP
        if(isDefaultImage) {g.setColor(Color.WHITE);}
        if(isActiveImage) {g.setColor(Color.YELLOW);}
        if(isPressedImage) {g.setColor(Color.RED);}

        g.drawRect(posX, posY, width, height);

        textArea.draw(g);
    }
}

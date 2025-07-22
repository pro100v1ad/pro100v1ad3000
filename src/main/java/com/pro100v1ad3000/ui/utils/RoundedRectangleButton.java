package main.java.com.pro100v1ad3000.ui.utils;

import main.java.com.pro100v1ad3000.systems.InputManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RoundedRectangleButton {

    private InputManager inputManager;

    private int rectX, rectY, rectWidth, rectHeight, cornerRadius;
    private BufferedImage defaultImage, activeImage, pressedButton;
    private boolean isDefaultButton, isActiveButton, isPressedButton;

    public RoundedRectangleButton(InputManager inputManager, int rectX, int rectY, int rectWidth, int rectHeight, int cornerRadius, String defaultImageName, String activeImageName, String pressedImageName) {
        this.rectX = rectX;
        this.rectY = rectY;
        this.rectWidth = rectWidth;
        this.rectHeight = rectHeight;
        this.cornerRadius = cornerRadius;
        loadImages(defaultImageName, activeImageName, pressedImageName);

        this.inputManager = inputManager;

    }

    private void loadImages(String defaultImageName, String activeImageName, String pressedImageName) {
        if(defaultImageName.isEmpty()) {

        } else {

        }

        if(activeImageName.isEmpty()) {

        } else {

        }

        if(pressedImageName.isEmpty()) {

        } else {

        }
    }




    private boolean isPointInRoundedRectangle(int mouseX, int mouseY) {

        if(mouseX < rectX || mouseX > rectX + rectWidth || mouseY < rectY || mouseY > mouseY + rectHeight) {
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

        return false;

    }

    public boolean update() {

        int mouseX = inputManager.getMouseX();
        int mouseY = inputManager.getMouseY();

        if(isPointInRoundedRectangle(mouseX, mouseY)) {
            isActiveButton = true;
            isDefaultButton = false;
        } else {
            isActiveButton = false;
            isDefaultButton = true;
        }

        return inputManager.isMouseButtonPressed(1) && isActiveButton;
    }

    public void draw(Graphics2D g) {
        if(isDefaultButton) g.drawImage(defaultImage, rectX, rectY, rectWidth, rectHeight, null);
        if(isActiveButton) g.drawImage(defaultImage, rectX, rectY, rectWidth, rectHeight, null);
        if(isPressedButton) g.drawImage(defaultImage, rectX, rectY, rectWidth, rectHeight, null);
    }

}

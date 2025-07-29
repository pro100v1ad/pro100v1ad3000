package main.java.com.pro100v1ad3000.ui.utils;

import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.systems.resources.AssetManager;
import main.java.com.pro100v1ad3000.utils.Config;


import java.awt.*;

public class InputFields {

    private InputManager inputManager;
    private AssetManager assetManager;
    private TextArea textArea;
    private int posX, posY, width, height, cornerRadius;
    private boolean isDefaultImage, isActiveImage, isPressedImage;

    public InputFields(InputManager inputManager, AssetManager assetManager, int posX, int posY, int width, int height) {
        this.inputManager = inputManager;
        this.assetManager = assetManager;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.textArea = new TextArea(inputManager, posX, posY, width, height);

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

    public void update(int currentWidth, int currentHeight) {
        textArea.update();

        int mouseX = inputManager.getMouseX();
        int mouseY = inputManager.getMouseY();

        int scaleMouseX = (int) (mouseX / (currentWidth / (float) Config.BASE_WIDTH));
        int scaleMouseY = (int) (mouseY / (currentHeight / (float) Config.BASE_HEIGHT));

        boolean flag = isPointInRoundedRectangle(scaleMouseX, scaleMouseY);
        if(flag && inputManager.isMouseButtonPressed(1)) {
            textArea.setActive(true);
            isPressedImage = true;
            isActiveImage = false;
            isDefaultImage = false;
        } else if(textArea.isActive() && inputManager.isMouseButtonPressed(1) && !flag) {
            textArea.setActive(false);
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
        inputManager.resetInputStates();
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

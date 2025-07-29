package main.java.com.pro100v1ad3000.ui.utils;

import javafx.scene.input.KeyCode;
import main.java.com.pro100v1ad3000.core.GameLoop;
import main.java.com.pro100v1ad3000.systems.InputManager;
import main.java.com.pro100v1ad3000.ui.fonts.FontManager;
import main.java.com.pro100v1ad3000.utils.Logger;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class TextArea {
    private boolean isActive;
    private String text, visibleText;
    private int startVisibleTextPosition;
    private int posX, posY, width, height;
    private int maxCharacters;
    private int posCursorCharacter;
    private int posCursorX;
    private Color textColor;
    private int fontSize;
    private final InputManager inputManager;
    private int selectionStart, selectionEnd;
    private boolean isVisibleTextCursor;
    private int currentTimeVisibleTextCursor, cursorBlinkingFrequency;

    public TextArea(InputManager inputManager, int posX, int posY, int width, int height) {
        this.isActive = false;
        this.inputManager = inputManager;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.maxCharacters = 100;
        this.textColor = Color.WHITE;
        this.fontSize = 16;
        this.text = " ";
        this.posCursorCharacter = 0;
        this.startVisibleTextPosition = 0;
        this.posCursorX = posX;
        this.selectionStart = -1;
        this.selectionEnd = -1;
        this.isVisibleTextCursor = false;
        this.currentTimeVisibleTextCursor = 0;
        this.cursorBlinkingFrequency = GameLoop.getTargetUps() / 2;
    }

    public void setText(String text) {
        this.text = text;
        if (!text.endsWith(" ")) {
            this.text += " ";
        }
        this.posCursorCharacter = Math.min(posCursorCharacter, this.text.length() - 1);
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setCursorBlinkingFrequency(int cursorBlinkingFrequency) {
        this.cursorBlinkingFrequency = cursorBlinkingFrequency;
    }

    private void updateTextCursor() {
        if (!isActive && isVisibleTextCursor) {
            isVisibleTextCursor = false;
        }
        if (isActive) {
            if (currentTimeVisibleTextCursor >= cursorBlinkingFrequency) {
                currentTimeVisibleTextCursor = 0;
                isVisibleTextCursor = !isVisibleTextCursor;
            } else {
                currentTimeVisibleTextCursor++;
            }
        }
    }

    public void update() {
        updateTextCursor();
        handleKeyInput();
        if (inputManager.isKeyPressed(KeyEvent.VK_RIGHT)) {
            moveCursorRight();
        }
        if (inputManager.isKeyPressed(KeyEvent.VK_LEFT)) {
            moveCursorLeft();
        }
        if (inputManager.isKeyPressed(KeyEvent.VK_BACK_SPACE)) {
            handleBackspace();
        }
        if (inputManager.isKeyPressed(KeyEvent.VK_DELETE)) {
            handleDelete();
        }
    }

    private boolean isShiftPressed = false;

    private void handleKeyInput() {
        // Обновляем состояние Shift
        isShiftPressed = inputManager.isKeyPressed(KeyEvent.VK_SHIFT);
        boolean isCtrlPressed = inputManager.isKeyPressed(KeyEvent.VK_CONTROL);

        for (Integer keyCode : inputManager.getPressedKeyCodes()) {

            // Игнорируем ввод букв, если нажат Ctrl
            if (isCtrlPressed && isLetterKey(keyCode)) {
                continue;
            }

            // Игнорируем сам Shift, чтобы не обрабатывать его как печатаемый символ
            if (keyCode == KeyEvent.VK_SHIFT) {
                continue;
            }

            for(char keyChar: inputManager.getTypedChars()) {
                if (text.length() < maxCharacters) {
                    // Вставляем символ в позицию курсора
                    text = text.substring(0, posCursorCharacter) + keyChar + text.substring(posCursorCharacter);
                    posCursorCharacter++;

                    // Если курсор находится в конце видимой области, сдвигаем видимую область вправо
                    FontMetrics fm = getFontMetrics();
                    if (posCursorCharacter >= startVisibleTextPosition + calculateMaxVisibleCharsFromStartPos(fm)) {
                        startVisibleTextPosition = posCursorCharacter - calculateMaxVisibleCharsFromStartPos(fm) + 1;
                    }
                }
            }

        }
    }

    // Вспомогательный метод для проверки, является ли клавиша буквой
    private boolean isLetterKey(int keyCode) {
        return (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z);
    }

    // Вспомогательный метод для получения FontMetrics
    private FontMetrics getFontMetrics() {
        // Создаем временный объект Graphics для получения FontMetrics
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = FontManager.getFont("defaultFont");
        if (font != null) {
            font = font.deriveFont((float) fontSize).deriveFont(Font.BOLD);
            g2d.setFont(font);
        } else {
            g2d.setFont(new Font("Arial", Font.PLAIN, fontSize).deriveFont(Font.BOLD));
        }
        FontMetrics fm = g2d.getFontMetrics();
        g2d.dispose();
        return fm;
    }

    private void setVisibleText(FontMetrics fm) {
        if (text.isEmpty()) {
            visibleText = "";
            startVisibleTextPosition = 0;
            return;
        }

        int maxVisibleChars = calculateMaxVisibleCharsFromStartPos(fm);

        // Если курсор находится за пределами видимой области, корректируем её
        if (posCursorCharacter < startVisibleTextPosition) {
            startVisibleTextPosition = posCursorCharacter;
        } else if (posCursorCharacter >= startVisibleTextPosition + maxVisibleChars) {
            startVisibleTextPosition = posCursorCharacter - maxVisibleChars + 1;
        }

        int endPos = Math.min(startVisibleTextPosition + maxVisibleChars, text.length());
        visibleText = text.substring(startVisibleTextPosition, endPos);
    }



    private int calculateMaxVisibleCharsFromStartPos(FontMetrics fm) {
        int currentWidth = 0;
        int endPos = startVisibleTextPosition;
        while (endPos < text.length() && currentWidth < width) {
            currentWidth += fm.charWidth(text.charAt(endPos));
            endPos++;
        }
        return endPos - startVisibleTextPosition;
    }

    private void moveCursorLeft() {
        if (posCursorCharacter > 0) {
            isVisibleTextCursor = true;
            currentTimeVisibleTextCursor = 0;
            posCursorCharacter--;
        }
    }

    private void moveCursorRight() {
        if (posCursorCharacter < text.length() - 1) {
            isVisibleTextCursor = true;
            currentTimeVisibleTextCursor = 0;
            posCursorCharacter++;
        }
    }

    private void handleBackspace() {
        if (posCursorCharacter > 0) {
            // Удаляем символ
            text = text.substring(0, posCursorCharacter - 1) + text.substring(posCursorCharacter);
            posCursorCharacter--;
            startVisibleTextPosition = Math.max(0, startVisibleTextPosition - 1);
        }
    }

    private void handleDelete() {
        if (posCursorCharacter < text.length() - 1) {
            text = text.substring(0, posCursorCharacter) + text.substring(posCursorCharacter + 1);
        }
    }

    private void handlePaste() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String pasteText = (String) clipboard.getData(DataFlavor.stringFlavor);
            if (pasteText != null && text.length() + pasteText.length() <= maxCharacters) {
                text = text.substring(0, posCursorCharacter) + pasteText + text.substring(posCursorCharacter);
                posCursorCharacter += pasteText.length();
            }
        } catch (Exception e) {
            Logger.error("Error pasting text: " + e.getMessage());
        }
    }

    private void handleCopy() {
        if (selectionStart != selectionEnd) {
            int start = Math.min(selectionStart, selectionEnd);
            int end = Math.max(selectionStart, selectionEnd);
            String selectedText = text.substring(start, end);
            StringSelection stringSelection = new StringSelection(selectedText);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }

    private void handleCut() {
        handleCopy();
        deleteSelectedText();
    }

    private void deleteSelectedText() {
        if (selectionStart != selectionEnd) {
            int start = Math.min(selectionStart, selectionEnd);
            int end = Math.max(selectionStart, selectionEnd);
            text = text.substring(0, start) + text.substring(end);
            posCursorCharacter = start;
            selectionStart = -1;
            selectionEnd = -1;
        }
    }

    private void drawSelectedText(Graphics2D g) {
        if (selectionStart != selectionEnd) {
            FontMetrics fm = g.getFontMetrics();
            int textHeight = fm.getHeight();
            int textX = posX;
            int textY = posY + textHeight;
            int start = Math.min(selectionStart, selectionEnd);
            int end = Math.max(selectionStart, selectionEnd);
            int startX = textX + fm.stringWidth(text.substring(startVisibleTextPosition, start));
            int endX = textX + fm.stringWidth(text.substring(startVisibleTextPosition, end));
            g.setColor(Color.BLUE);
            g.fillRect(startX, posY, endX - startX, textHeight);
            g.setColor(Color.WHITE);
            g.drawString(text.substring(start, end), startX, textY);
        }
    }

    private void drawTextCursor(Graphics2D g) {
        if (isVisibleTextCursor) {
            FontMetrics fm = g.getFontMetrics();
            int cursorRelativePos = posCursorCharacter - startVisibleTextPosition;
            posCursorX = posX + fm.stringWidth(visibleText.substring(0, cursorRelativePos));
            g.setColor(Color.WHITE);
            g.drawLine(posCursorX, posY, posCursorX, posY + height);
        }
    }

    private void drawText(Graphics2D g) {
        if (text != null && !text.isEmpty()) {
            Font font = FontManager.getFont("defaultFont");
            if (font != null) {
                font = font.deriveFont((float) fontSize).deriveFont(Font.BOLD);
                g.setFont(font);
            } else {
                g.setFont(new Font("Arial", Font.PLAIN, fontSize).deriveFont(Font.BOLD));
            }
            FontMetrics fm = g.getFontMetrics();
            setVisibleText(fm);
            g.setColor(textColor);
            g.drawString(visibleText, posX, posY + fm.getHeight());
        }
    }

    public void draw(Graphics2D g) {
        drawText(g);
        drawSelectedText(g);
        drawTextCursor(g);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        currentTimeVisibleTextCursor = 0;
        isVisibleTextCursor = true;
        if (!text.endsWith(" ")) {
            text += " ";
        }
        posCursorCharacter = Math.max(0, text.length() - 1);
    }
}

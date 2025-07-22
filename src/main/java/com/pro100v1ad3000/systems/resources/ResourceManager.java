package main.java.com.pro100v1ad3000.systems.resources;

import main.java.com.pro100v1ad3000.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResourceManager {

    private Map<String, BufferedImage> sprites; // Хранит вырезанные спрайты
    private Map<String, BufferedImage> spriteSheets; // Хранит загруженные спрайт-листы
    private Map<String, SpriteMetaData> spriteMetaData; // Хранит метаданные о спрайтах

    public ResourceManager() {
        sprites = new HashMap<>();
        spriteSheets = new HashMap<>();
        spriteMetaData = new HashMap<>();
    }

    // Загрузка спрайт-листа
    public void loadSpriteSheet(String key, String path) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            if (inputStream == null) {
                Logger.error("LoadSpriteSheet: empty path or resource not found - " + path);
                return;
            }
            BufferedImage spriteSheet = ImageIO.read(inputStream);
            if (spriteSheet != null) {
                spriteSheets.put(key, spriteSheet);
            } else {
                Logger.error("LoadSpriteSheet: failed to load sprite sheet from path - " + path);
            }
        } catch (IOException e) {
            Logger.error("LoadSpriteSheet error: " + e.getMessage());
        }
    }

    // Регистрация спрайта
    public void registerSprite(String spriteKey, String spriteSheetKey, int spriteNumber, int spriteWidth, int spriteHeight) {
        if (spriteWidth <= 0 || spriteHeight <= 0) {
            Logger.error("RegisterSprite: invalid sprite dimensions for sprite key - " + spriteKey);
            return;
        }
        spriteMetaData.put(spriteKey, new SpriteMetaData(spriteSheetKey, spriteNumber, spriteWidth, spriteHeight));
    }

    // Получение спрайта по ключу
    public BufferedImage getSprite(String spriteKey) {
        if (sprites.containsKey(spriteKey)) {
            return sprites.get(spriteKey);
        } else if (spriteMetaData.containsKey(spriteKey)) {
            SpriteMetaData metaData = spriteMetaData.get(spriteKey);
            BufferedImage spriteSheet = spriteSheets.get(metaData.spriteSheetKey);
            if (spriteSheet != null) {
                BufferedImage sprite = extractSprite(spriteSheet, metaData.spriteNumber, metaData.spriteWidth, metaData.spriteHeight);
                sprites.put(spriteKey, sprite); // Кэшируем вырезанный спрайт
                return sprite;
            } else {
                Logger.error("GetSprite: sprite sheet not found for key - " + metaData.spriteSheetKey);
            }
        } else {
            Logger.error("GetSprite: sprite key not found - " + spriteKey);
        }
        return null;
    }

    // Извлечение спрайта из спрайт-листа
    private BufferedImage extractSprite(BufferedImage spriteSheet, int spriteNumber, int spriteWidth, int spriteHeight) {
        int spritesPerRow = spriteSheet.getWidth() / spriteWidth;
        int spriteX = (spriteNumber % spritesPerRow) * spriteWidth;
        int spriteY = (spriteNumber / spritesPerRow) * spriteHeight;
        return spriteSheet.getSubimage(spriteX, spriteY, spriteWidth, spriteHeight);
    }

    // Внутренний класс для хранения метаданных спрайта
    private static class SpriteMetaData {
        String spriteSheetKey;
        int spriteNumber;
        int spriteWidth;
        int spriteHeight;

        SpriteMetaData(String spriteSheetKey, int spriteNumber, int spriteWidth, int spriteHeight) {
            this.spriteSheetKey = spriteSheetKey;
            this.spriteNumber = spriteNumber;
            this.spriteWidth = spriteWidth;
            this.spriteHeight = spriteHeight;
        }
    }

}

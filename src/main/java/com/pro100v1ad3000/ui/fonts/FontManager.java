package main.java.com.pro100v1ad3000.ui.fonts;

import main.java.com.pro100v1ad3000.utils.Logger;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {

    private static Map<String, Font> fonts;

    public FontManager() {
        fonts = new HashMap<>();

        fonts.put("defaultFont", loadFont("/main/resources/assets/fonts/defaultFont1.ttf"));

    }

    private Font loadFont(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is != null) {
                return Font.createFont(Font.TRUETYPE_FONT, is);
            }
            Logger.info("Cannot find file: " + path);
        } catch (FontFormatException | IOException e) {
            Logger.error("Error loadFont(): " + e.getMessage());
        }

        return null;
    }

    public static Font getFont(String key) {
        if(fonts == null) return null;
        return fonts.getOrDefault(key, null);
    }
}

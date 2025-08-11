package main.java.com.pro100v1ad3000.systems.language;

import main.java.com.pro100v1ad3000.utils.GameSettings;
import main.java.com.pro100v1ad3000.utils.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LanguageManager {

    private static String currentLanguage = GameSettings.getProperty("language");
    private static final Map<String, String> languageTexts = new HashMap<>();

    private static void loadLanguageTexts(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream input = LanguageManager.class.getResourceAsStream(filePath)) {
            if(input == null) {
                Logger.warn("Cannot find resource: " + filePath);
                return;
            }
            Map<String, String> texts = objectMapper.readValue(input, Map.class);
            languageTexts.putAll(texts);
        } catch (Exception e) {
            Logger.error("Error loading language texts: " + e.getMessage());
        }

    }

    public static void setLanguage(String language) {
        currentLanguage = language;
        GameSettings.setProperty("language", currentLanguage);
        GameSettings.saveProperties();
        languageTexts.clear();
    }

    private static String convertToResourcePath(String path) { // menu.startMenu.buttons.exit_button

        String[] parts = path.split("\\.");

        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid input format. Expected format: 'texts.key'.");
        }

        String filePath = "/main/resources/localized/" + currentLanguage + "/";

        for(int i = 0; i < parts.length - 2; i++) {
            filePath += parts[i] + "/";
        }

        filePath += parts[parts.length-2].replace(".", "/") + ".json";

        return filePath;
    }

    public static String getText(String keyPath) {

        String key = keyPath.split("\\.")[keyPath.split("\\.").length-1];

        if(languageTexts.containsKey(key)) {
            return languageTexts.getOrDefault(key, key);
        } else {
            loadLanguageTexts(convertToResourcePath(keyPath));
            return languageTexts.getOrDefault(key, key);
        }
    }

}

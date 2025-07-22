package main.java.com.pro100v1ad3000.systems.language;

import main.java.com.pro100v1ad3000.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LanguageManager {

    /*
    Я ему устанавливаю язык в game.properties,
    а он дает мне весь текст нужного языка по ключу
     */

    private String currentLanguage;
    private final Map<String, String> languageTexts;

    public LanguageManager() {

        currentLanguage = "ru";
        languageTexts = new HashMap<>();

    }

    public void reloadLanguage() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/main/resources/config/game.properties")) {
            if(input == null) {
                throw new RuntimeException("Cannot find resource: /main/resources/config/game.properties");
            }
            properties.load(input);
            this.currentLanguage = properties.getProperty("language");
        } catch (IOException e) {
            Logger.error("Error in loadLanguage: " + e.getMessage());
        }
    }

    private void loadLanguageTexts(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream input = getClass().getResourceAsStream(filePath)) {
            if(input == null) {
                throw new RuntimeException("Cannot find resource: " + filePath);
            }
            Map<String, String> texts = objectMapper.readValue(input, Map.class);
            languageTexts.putAll(texts);
        } catch (Exception e) {
            Logger.error("Error loading language texts: " + e.getMessage());
        }

    }

    private String convertToResourcePath(String path) {

        String[] parts = path.split("\\.");

        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid input format. Expected format: 'texts.key'.");
        }

        String filePath = parts[parts.length-2];


        return "/main/resources/localized/" + currentLanguage + "/" + filePath.replace(".", "/") + ".json";
    }

    /* Пример получения текста getText("texts.menu.start_button"),
    где text.menu - путь к json файлу menu.json,
    а start_button - полу в этом файле
    */
    public String getText(String keyPath) {

        String key = keyPath.split("\\.")[keyPath.split("\\.").length-1];

        if(languageTexts.containsKey(key)) {
            return languageTexts.getOrDefault(key, key);
        } else {
            loadLanguageTexts(convertToResourcePath(keyPath));
            return languageTexts.getOrDefault(key, key);
        }
    }



}

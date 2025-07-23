package main.java.com.pro100v1ad3000.systems.language;

import main.java.com.pro100v1ad3000.utils.Config;
import main.java.com.pro100v1ad3000.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LanguageManager {

    private String currentLanguage;
    private final Map<String, String> languageTexts;

    public LanguageManager() {

        languageTexts = new HashMap<>();
        reloadLanguage();

    }

    public void reloadLanguage() {
        Properties properties = new Properties();
        Path path = Paths.get(Config.PATH_TO_GAME_PROPERTIES);
        try (InputStream input = Files.newInputStream(path)) {
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
                Logger.warn("Cannot find resource: " + filePath);
                return;
            }
            Map<String, String> texts = objectMapper.readValue(input, Map.class);
            languageTexts.putAll(texts);
        } catch (Exception e) {
            Logger.error("Error loading language texts: " + e.getMessage());
        }

    }

    public void setLanguage(String language) {
        this.currentLanguage = language;
        saveLanguageToProperties();
        reloadLanguage();
        languageTexts.clear();
    }

    private void saveLanguageToProperties() {
        Properties properties = new Properties();
        Path propertiesPath = Paths.get(Config.PATH_TO_GAME_PROPERTIES);

        // Загружаем существующие свойства из файла
        try (InputStream input = Files.newInputStream(propertiesPath)) {
            properties.load(input);
        } catch (IOException e) {
            Logger.error("Error loading properties: " + e.getMessage());
            return; // Если не удалось загрузить, выходим из метода
        }

        // Изменяем только свойство language
        properties.setProperty("language", currentLanguage);

        // Сохраняем все свойства обратно в файл
        try (OutputStream output = Files.newOutputStream(propertiesPath)) {
            properties.store(output, "Language settings");
        } catch (IOException e) {
            Logger.error("Error saving language to properties: " + e.getMessage());
        }
    }


    private String convertToResourcePath(String path) { // menu.startMenu.buttons.exit_button

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

    public String getText(String keyPath) {

        String key = keyPath.split("\\.")[keyPath.split("\\.").length-1];

        if(languageTexts.containsKey(key)) {
            return languageTexts.getOrDefault(key, key);
        } else {
            loadLanguageTexts(convertToResourcePath(keyPath));
            return languageTexts.getOrDefault(key, key);
        }
    }

    public String getCurrentLanguage() {
        return currentLanguage;
    }

}

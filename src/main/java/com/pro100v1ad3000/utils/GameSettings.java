package main.java.com.pro100v1ad3000.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class GameSettings {
    private static final Properties properties = new Properties();
    private static final Path configFilePath = Paths.get(Config.PATH_TO_GAME_PROPERTIES);
    private static final String defaultConfigFile = "/main/resources/config/defaultGame.properties";

    static  {
        initializeFile();
        loadProperties();
    }

    private static void initializeFile() {
        try {
            // Создаем директорию, если она не существует
            if (!Files.exists(configFilePath.getParent())) {
                Files.createDirectories(configFilePath.getParent());
            }

            // Копируем файл настроек по умолчанию, если его нет
            if (!Files.exists(configFilePath)) {
                try (InputStream defaultConfigStream = GameSettings.class.getResourceAsStream(defaultConfigFile)) {
                    if (defaultConfigStream == null) {
                        throw new FileNotFoundException("Default configuration file not found in resources.");
                    }
                    Files.copy(defaultConfigStream, configFilePath);
                }
            }
        } catch (IOException e) {
            Logger.error("Error initializing game.properties file: " + e.getMessage());
        }
    }

    private static void loadProperties() {
        try (InputStream input = Files.newInputStream(configFilePath)) {
            properties.load(input);
        } catch (IOException ex) {
            Logger.error("Error loading properties file: " + ex.getMessage());
        }
    }

    public static String getProperty(String key) {
        // Пытаемся получить значение из текущих свойств
        String value = properties.getProperty(key);
        if (value == null) {
            value = getDefaultProperty(key);
        }
        // Возвращаем найденное значение или пустую строку, если значение не найдено
        return value != null ? value : "";
    }

    private static String getDefaultProperty(String key) {
        Properties defaultProperties = new Properties();
        try (InputStream input = GameSettings.class.getResourceAsStream(defaultConfigFile)) {
            if (input == null) {
                throw new IOException("Default configuration file not found in resources.");
            }
            defaultProperties.load(input);
            return defaultProperties.getProperty(key);
        } catch (IOException e) {
            Logger.error("Error loading default properties: " + e.getMessage());
            return null;
        }
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public static void saveProperties() {
        try (OutputStream output = Files.newOutputStream(configFilePath)) {
            properties.store(output, "Game Settings");
        } catch (IOException ex) {
            Logger.error("Error saving properties file: " + ex.getMessage());
        }
    }

    public static void resetToDefault() {
        try (InputStream defaultConfigStream = GameSettings.class.getResourceAsStream(defaultConfigFile)) {
            if (defaultConfigStream == null) {
                throw new FileNotFoundException("Default configuration file not found in resources.");
            }
            properties.load(defaultConfigStream);
            saveProperties();
        } catch (IOException e) {
            Logger.error("Error resetting to default properties: " + e.getMessage());
        }
    }
}

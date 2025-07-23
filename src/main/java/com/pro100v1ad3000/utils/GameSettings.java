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
    private Properties properties;
    private final Path configFilePath;
    private final String defaultConfigFile = "/main/resources/config/defaultGame.properties";

    public GameSettings(String configFilePath) {
        this.configFilePath = Paths.get(configFilePath);
        this.properties = new Properties();
        initializeConfigFile();
        loadProperties();
    }

    private void initializeConfigFile() {
        try {
            // Создаем директорию, если она не существует
            if (!Files.exists(configFilePath.getParent())) {
                Files.createDirectories(configFilePath.getParent());
            }

            // Копируем файл настроек по умолчанию, если его нет
            if (!Files.exists(configFilePath)) {
                try (InputStream defaultConfigStream = getClass().getResourceAsStream(defaultConfigFile)) {
                    if (defaultConfigStream == null) {
                        throw new FileNotFoundException("Default configuration file not found in resources.");
                    }
                    Files.copy(defaultConfigStream, configFilePath);
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing configuration file: " + e.getMessage());
        }
    }

    private void loadProperties() {
        try (InputStream input = Files.newInputStream(configFilePath)) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Error loading properties file: " + ex.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public void saveProperties() {
        try (OutputStream output = Files.newOutputStream(configFilePath)) {
            properties.store(output, "Game Settings");
        } catch (IOException ex) {
            System.err.println("Error saving properties file: " + ex.getMessage());
        }
    }

    public void resetToDefault() {
        try (InputStream defaultConfigStream = getClass().getResourceAsStream(defaultConfigFile)) {
            if (defaultConfigStream == null) {
                throw new FileNotFoundException("Default configuration file not found in resources.");
            }
            properties.load(defaultConfigStream);
            saveProperties();
        } catch (IOException e) {
            System.err.println("Error resetting to default properties: " + e.getMessage());
        }
    }
}

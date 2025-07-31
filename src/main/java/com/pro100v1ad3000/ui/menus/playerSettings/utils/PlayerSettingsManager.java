package main.java.com.pro100v1ad3000.ui.menus.playerSettings.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.pro100v1ad3000.utils.Config;
import main.java.com.pro100v1ad3000.utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PlayerSettingsManager {

    private final String PATH_TO_DEFAULT_PLAYER_SETTINGS = "/main/resources/config/player/defaultPlayerSettings.json";
    private final String PATH_TO_SAVE_PLAYER_SETTINGS = Config.PATH_TO_PLAYER_SETTINGS_JSON;
    private final Path configFilePath;

    private final Map<Integer, PlayerDataManager> playersDataMap;
    private final Map<Integer, Boolean> playersActiveStatus;


    public PlayerSettingsManager() {

        this.playersDataMap = new HashMap<>();
        this.playersActiveStatus = new HashMap<>();

        this.configFilePath = Paths.get(PATH_TO_SAVE_PLAYER_SETTINGS);

        if(!Files.exists(configFilePath)) { // Проверка существования файла
            Logger.warn("The player's configuration file was not found");
            initializeFile();
            loadDefaultPlayer();
        }

        if(Files.exists(configFilePath)) {
            Logger.info("The player's configuration file has been found");
            loadRecords();
        }

    }

    private void initializeFile() {
        try {
            // Создаем директорию, если она не существует
            if (!Files.exists(configFilePath.getParent())) {
                Files.createDirectories(configFilePath.getParent());
            }

            // Копируем файл настроек по умолчанию, если его нет
            if (!Files.exists(configFilePath)) {
                try (InputStream defaultConfigStream = getClass().getResourceAsStream(PATH_TO_DEFAULT_PLAYER_SETTINGS)) {
                    if (defaultConfigStream == null) {
                        throw new FileNotFoundException("Default configuration file not found in resources.");
                    }
                    Files.copy(defaultConfigStream, configFilePath);
                }
            }
            Logger.info("Successful file creation: " + configFilePath);
        } catch (IOException e) {
            Logger.error("Error initializing playerSettings.json file: " + e.getMessage());
        }
    }

    private boolean checkIntegrityRecord(Map.Entry<String, Map<String, Object>> entry) {

        String playerId = entry.getKey();
        Map<String, Object> playerInfo = entry.getValue();

        if (!playerInfo.containsKey("nickname") || !playerInfo.containsKey("playerSkin") || !playerInfo.containsKey("isActive")) {
            Logger.warn("Missing required fields for player ID: " + playerId);
            return false;
        }

        Map<String, Object> playerSkin = (Map<String, Object>) playerInfo.get("playerSkin");
        if (!playerSkin.containsKey("headColor") || !playerSkin.containsKey("bodyColor") || !playerSkin.containsKey("handColor")) {
            Logger.warn("Missing required skin fields for player ID: " + playerId);
            return false;
        }

        return true;
    }

    // Загружает все записи из файла
    private void loadRecords() {
        ObjectMapper objectMapper = new ObjectMapper();

        Path path = Paths.get(PATH_TO_SAVE_PLAYER_SETTINGS);
        try (InputStream input = Files.newInputStream(path)) {
            Map<String, Map<String, Object>> playersData = objectMapper.readValue(input, objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, HashMap.class));

            if (playersData.isEmpty()) { // Проверка на пустоту файла
                Logger.warn("The file is empty: " + configFilePath);
                loadDefaultPlayer();
            }

            for (Map.Entry<String, Map<String, Object>> entry : playersData.entrySet()) {

                if(!checkIntegrityRecord(entry)) {
                    continue;
                }

                int playerId = getPlayerId(entry.getKey());
                while (playersDataMap.containsKey(playerId)) { // Проверка на уникальность id
                    int newPlayerId = getPlayerId(entry.getKey());
                    Logger.warn("re-creating the ID: " + playerId + " --> " + newPlayerId);
                    playerId = newPlayerId;
                }


                Map<String, Object> playerInfo = entry.getValue();

                String nickname = (String) playerInfo.get("nickname");

                Logger.debug("load id: " + playerId + ", nickname: " + nickname);

                Boolean isActive = (Boolean) playerInfo.get("isActive");
                if(isActive && playersActiveStatus.containsValue(true)) { // Проверка на уникальность значения true
                    isActive = false;
                }

                Map<String, Object> playerSkin = (Map<String, Object>) playerInfo.get("playerSkin");
                String headColor = (String) playerSkin.get("headColor");
                String bodyColor = (String) playerSkin.get("bodyColor");
                String handColor = (String) playerSkin.get("handColor");

                playersDataMap.put(playerId, new PlayerDataManager(nickname, headColor, bodyColor, handColor));
                playersActiveStatus.put(playerId, isActive);

            }

            if(playersDataMap.isEmpty()) {
                loadDefaultPlayer();
            }

        } catch (Exception e) {
            Logger.error("Error when downloading the player configuration file: " + e.getMessage());
        }
    }

    private void loadDefaultPlayer() {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream input = getClass().getResourceAsStream(PATH_TO_DEFAULT_PLAYER_SETTINGS)) {
            if (input == null) {
                Logger.error("File not found: " + PATH_TO_DEFAULT_PLAYER_SETTINGS);
                throw new FileNotFoundException("File not found: " + PATH_TO_DEFAULT_PLAYER_SETTINGS);
            }

            Map<String, Map<String, Object>> playersData = objectMapper.readValue(input, objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, HashMap.class));

            if (playersData.isEmpty()) { // Проверка на пустоту файла
                Logger.warn("The file is empty: " + configFilePath);
            }

            for (Map.Entry<String, Map<String, Object>> entry : playersData.entrySet()) {

                if(!checkIntegrityRecord(entry)) {
                    continue;
                }

                int playerId = getPlayerId(entry.getKey());
                while (playersDataMap.containsKey(playerId)) { // Проверка на уникальность id
                    int newPlayerId = getPlayerId(entry.getKey());
                    Logger.warn("re-creating the ID: " + playerId + " --> " + newPlayerId);
                    playerId = newPlayerId;
                }


                Map<String, Object> playerInfo = entry.getValue();

                String nickname = (String) playerInfo.get("nickname");

                Logger.debug("load defaultPlayer id: " + playerId + ", nickname: " + nickname);

                Boolean isActive = (Boolean) playerInfo.get("isActive");
                if(isActive && playersActiveStatus.containsValue(true)) { // Проверка на уникальность значения true
                    isActive = false;
                }

                Map<String, Object> playerSkin = (Map<String, Object>) playerInfo.get("playerSkin");
                String headColor = (String) playerSkin.get("headColor");
                String bodyColor = (String) playerSkin.get("bodyColor");
                String handColor = (String) playerSkin.get("handColor");

                playersDataMap.put(playerId, new PlayerDataManager(nickname, headColor, bodyColor, handColor));
                playersActiveStatus.put(playerId, isActive);

            }

        } catch (Exception e) {
            Logger.error("Error verifying the existence of the player's default configuration file: " + e.getMessage());
        }
    }

    private int getPlayerId(String playerId) {

        try {
            return Integer.parseInt(playerId);
        } catch (Exception e) {
            return (int) (Math.random() * 900000) + 100000;
        }

    }

    private void saveLoadPlayersData() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Создаем структуру данных для сохранения
        Map<String, Object> playersData = new HashMap<>();

        for (Map.Entry<Integer, PlayerDataManager> entry : playersDataMap.entrySet()) {
            Integer playerId = entry.getKey();
            PlayerDataManager playerData = entry.getValue();

            Map<String, Object> playerInfo = new HashMap<>();
            playerInfo.put("nickname", playerData.getNickname());

            Map<String, String> playerSkin = new HashMap<>();
            playerSkin.put("headColor", playerData.getHeadColor());
            playerSkin.put("bodyColor", playerData.getBodyColor());
            playerSkin.put("handColor", playerData.getHandColor());

            playerInfo.put("playerSkin", playerSkin);
            playerInfo.put("isActive", playersActiveStatus.getOrDefault(playerId, false));

            playersData.put(playerId.toString(), playerInfo);
        }

        // Записываем данные в файл
        try {
            objectMapper.writeValue(Files.newOutputStream(configFilePath), playersData);
        } catch (IOException e) {
            Logger.error("Error saving player data to file: " + e.getMessage());
        }
    }

    public void dispose() {
        saveLoadPlayersData();
    }

}

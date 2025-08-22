package main.java.com.game.world.entities.players.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import main.java.com.game.utils.Config;
import main.java.com.game.utils.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PlayerInfoSaveManager {
    private static PlayerInfoSaveManager instance;

    private final Map<PlayerInfo, Boolean> players;
    private final Path configPath;
    private final String defaultConfigPath = Config.PATH_TO_PLAYER_SETTINGS_JSON;

    // Приватный конструктор
    private PlayerInfoSaveManager() {
        this.players = new HashMap<>();
        this.configPath = Paths.get(defaultConfigPath);
        loadPlayers();
    }

    // Получение экземпляра Singleton
    public static synchronized PlayerInfoSaveManager getInstance() {
        if (instance == null) {
            instance = new PlayerInfoSaveManager();
        }
        return instance;
    }

    // Загрузка игроков из JSON
    private void loadPlayers() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Проверяем существование файла
            if (!Files.exists(configPath)) {
                createDefaultConfig();
                return;
            }

            // Проверяем, не пустой ли файл
            if (Files.size(configPath) == 0) {
                createDefaultConfig();
                return;
            }

            // Читаем JSON
            Map<String, Map<String, Object>> playersData = mapper.readValue(
                    Files.newInputStream(configPath),
                    mapper.getTypeFactory().constructMapType(HashMap.class, String.class, HashMap.class)
            );

            // Обрабатываем данные
            if (playersData.isEmpty()) {
                createDefaultConfig();
                return;
            }

            boolean hasActivePlayer = false;
            for (Map.Entry<String, Map<String, Object>> entry : playersData.entrySet()) {
                try {
                    String idStr = entry.getKey();
                    Map<String, Object> playerData = entry.getValue();

                    // Проверяем обязательные поля
                    if (!playerData.containsKey("nickname") || !playerData.containsKey("isActive")) {
                        continue; // Пропускаем поврежденные записи
                    }

                    int id = Integer.parseInt(idStr);
                    String nickname = (String) playerData.get("nickname");
                    boolean isActive = (Boolean) playerData.get("isActive");

                    PlayerInfo playerInfo = new PlayerInfo(nickname, id);

                    // Проверяем уникальность ID
                    if (players.keySet().stream().anyMatch(p -> p.getId() == id)) {
                        continue; // Пропускаем дубликаты ID
                    }

                    players.put(playerInfo, isActive);

                    // Проверяем активных игроков
                    if (isActive) {
                        if (hasActivePlayer) {
                            players.put(playerInfo, false); // Только один активный игрок
                        } else {
                            hasActivePlayer = true;
                        }
                    }
                } catch (Exception e) {
                    // Пропускаем поврежденные записи
                    continue;
                }
            }

            // Если нет активных игроков, назначаем случайного
            if (!hasActivePlayer && !players.isEmpty()) {
                setRandomPlayerActive();
            }

            // Если все записи были пропущены, создаем дефолтного игрока
            if (players.isEmpty()) {
                createDefaultConfig();
            }
        } catch (IOException e) {
            // Если файл поврежден или недоступен, создаем дефолтного игрока
            createDefaultConfig();
        }
    }

    // Создание дефолтного игрока
    private void createDefaultConfig() {
        players.clear();
        PlayerInfo defaultPlayer = new PlayerInfo();
        players.put(defaultPlayer, true);
        savePlayers();
    }

    // Назначение случайного игрока активным
    private void setRandomPlayerActive() {
        List<PlayerInfo> playerList = new ArrayList<>(players.keySet());
        if (!playerList.isEmpty()) {
            PlayerInfo randomPlayer = playerList.get(new Random().nextInt(playerList.size()));
            players.replaceAll((k, v) -> false);
            players.put(randomPlayer, true);
        }
    }

    // Сохранение игроков в JSON
    public void savePlayers() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, Object>> playersData = new HashMap<>();

        for (Map.Entry<PlayerInfo, Boolean> entry : players.entrySet()) {
            PlayerInfo player = entry.getKey();
            Map<String, Object> playerData = new HashMap<>();
            playerData.put("nickname", player.getNickname());
            playerData.put("isActive", entry.getValue());
            playersData.put(String.valueOf(player.getId()), playerData);
        }

        try {
            // Создаем директорию, если её нет
            if (!Files.exists(configPath.getParent())) {
                Files.createDirectories(configPath.getParent());
            }

            // Сохраняем JSON
            mapper.writeValue(Files.newOutputStream(configPath), playersData);
        } catch (IOException e) {
            System.err.println("Error saving players: " + e.getMessage());
        }
    }

    // Добавление нового игрока
    public void addPlayer(PlayerInfo playerInfo) {
        // Проверяем уникальность никнейма
        PlayerInfo finalPlayerInfo = playerInfo;
        boolean nicknameExists = players.keySet().stream()
                .anyMatch(p -> p.getNickname().equalsIgnoreCase(finalPlayerInfo.getNickname()));

        if (nicknameExists) {
            Logger.info("Player with nickname '" + playerInfo.getNickname() + "' already exists. Ignoring.");
            return; // Игнорируем игрока с существующим никнеймом
        }

        // Проверяем уникальность ID
        PlayerInfo finalPlayerInfo1 = playerInfo;
        if (players.keySet().stream().anyMatch(p -> p.getId() == finalPlayerInfo1.getId())) {
            playerInfo = new PlayerInfo(playerInfo.getNickname(), PlayerInfo.generateRandomId());
        }

        players.put(playerInfo, false);

        // Делаем нового активным
        setActivePlayer(playerInfo);

        savePlayers();
    }


    // Установка активного игрока
    public void setActivePlayer(PlayerInfo playerInfo) {
        players.replaceAll((k, v) -> false);
        players.put(playerInfo, true);
        savePlayers();
    }

    // Получение активного игрока
    public PlayerInfo getActivePlayer() {
        for (Map.Entry<PlayerInfo, Boolean> entry : players.entrySet()) {
            if (entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }

    // Получение всех игроков
    public Map<PlayerInfo, Boolean> getAllPlayers() {
        return Collections.unmodifiableMap(players);
    }

    // Получение игрока по ID
    public PlayerInfo getPlayerById(int id) {
        for (PlayerInfo player : players.keySet()) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }
}

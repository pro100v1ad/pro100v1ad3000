package main.java.com.game.world.chunk;


/*
Управляет чанками.
Решает какой чанк нужно выгрузить ( сохранить изменения в файл и удалить с памяти оперативной) - через ChunkSerialization
и какой чанк загрузить ( сгенерировать terrain для чанка, а затем подгрузить изменения если имеются. - через ChunkSerialization
Также метод для выгрузки всех чанков ( например при выходе из игры или мира).
Также метод для простого сохранения информации о чанках ( автосохранение).

Класс возвращает всю информацию и загруженных чанках при запросе. - getChunk(x, y)
При необходимости изменяет информацию о чанке при его изменении игроком. - setChunk(x, y)
 */
import main.java.com.game.world.World;
import main.java.com.game.world.WorldConfig;

import java.util.HashMap;
import java.util.Map;

public class ChunkManager {
    private final World world;
    private final Map<ChunkCoordinates, Chunk> loadedChunks;
    private final int renderDistance; // Сколько чанков подгружать вокруг игрока

    public ChunkManager(World world) {
        this.world = world;
        this.loadedChunks = new HashMap<>();
        this.renderDistance = 3; // Например, 3 чанка вокруг игрока

        loadChunks((int)world.getLocalPlayer().getX(), (int)world.getLocalPlayer().getY());
    }

    public void update(int playerX, int playerY) {
        WorldConfig config = world.getConfig();
        int chunkSize = 32;
        int playerChunkX = playerX / chunkSize;
        int playerChunkY = playerY / chunkSize;

        // Выгружаем чанки, которые слишком далеко от игрока
        loadedChunks.entrySet().removeIf(entry -> {
            ChunkCoordinates coords = entry.getKey();
            int dx = Math.abs(coords.getX() - playerChunkX);
            int dy = Math.abs(coords.getY() - playerChunkY);
            return dx > renderDistance || dy > renderDistance;
        });

        // Загружаем чанки вокруг игрока
        loadChunks(playerChunkX, playerChunkY);
    }

    private void loadChunks(int playerChunkX, int playerChunkY) {
        for (int x = playerChunkX - renderDistance; x <= playerChunkX + renderDistance; x++) {
            for (int y = playerChunkY - renderDistance; y <= playerChunkY + renderDistance; y++) {
                ChunkCoordinates coords = new ChunkCoordinates(x, y);
                if (!loadedChunks.containsKey(coords)) {
                    Chunk chunk = generateChunk(coords);
                    loadedChunks.put(coords, chunk);
                }
            }
        }
    }

    private Chunk generateChunk(ChunkCoordinates coords) {
        // Генерация чанка (пока просто создаём пустой)
        Chunk chunk = new Chunk(coords, world.getConfig());
        // Здесь будет вызов генератора террейна
        return chunk;
    }

    public Chunk getChunk(int x, int y) {
        return loadedChunks.get(new ChunkCoordinates(x, y));
    }

    public Map<ChunkCoordinates, Chunk> getLoadedChunks() {
        return loadedChunks;
    }
}

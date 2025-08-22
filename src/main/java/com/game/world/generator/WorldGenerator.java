package main.java.com.game.world.generator;

import main.java.com.game.world.World;
import main.java.com.game.world.chunk.Chunk;
import main.java.com.game.world.generator.terrain.TerrainGenerator;

/*
Отвечает за генерацию самого мира:
    имеет метод для генерации чанка.
 */


public class WorldGenerator {
    private final TerrainGenerator terrainGenerator;

    public WorldGenerator(long seed) {
        this.terrainGenerator = new TerrainGenerator(seed);
    }

    public void generateWorld(World world) {
        // Генерация мира будет происходить динамически при загрузке чанков
    }

    public void generateChunk(Chunk chunk) {
        terrainGenerator.generateTerrain(chunk);
    }
}




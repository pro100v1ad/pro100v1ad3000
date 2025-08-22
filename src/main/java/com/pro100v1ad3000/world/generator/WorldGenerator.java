package main.java.com.pro100v1ad3000.world.generator;

import main.java.com.pro100v1ad3000.world.World;
import main.java.com.pro100v1ad3000.world.WorldConfig;
import main.java.com.pro100v1ad3000.world.chunk.Chunk;
import main.java.com.pro100v1ad3000.world.generator.terrain.TerrainGenerator;

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




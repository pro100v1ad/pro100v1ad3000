package main.java.com.game.world.generator.terrain;

/*
Класс содержащий в себе информацию о генерации местности в определенной области
    карта местности
    карта температур
    и тд
с возможностью доступа к этой информации.
 */


import main.java.com.game.world.WorldConfig;
import main.java.com.game.world.chunk.Chunk;
import main.java.com.game.world.generator.noice.PerlinNoise;

public class TerrainGenerator {

    private final PerlinNoise noise;

    public TerrainGenerator(long seed) {
        this.noise = new PerlinNoise(seed);
    }

    public void generateTerrain(Chunk chunk) {
        WorldConfig config = chunk.getConfig();
        int chunkSize = 32;
        int chunkX = chunk.getCoordinates().getX();
        int chunkY = chunk.getCoordinates().getY();

        for (int x = 0; x < chunkSize; x++) {
            for (int y = 0; y < chunkSize; y++) {
                double worldX = (chunkX * chunkSize + x) / 100.0;
                double worldY = (chunkY * chunkSize + y) / 100.0;
                double noiseValue = noise.noise(worldX, worldY);

                // Пример: если noiseValue > 0, то блок - трава, иначе - вода
                int blockId = noiseValue > 0 ? 1 : 0;
                chunk.setBlock(x, y, blockId);
            }
        }
    }

}

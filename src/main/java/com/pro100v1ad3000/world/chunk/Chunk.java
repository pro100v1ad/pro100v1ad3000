package main.java.com.pro100v1ad3000.world.chunk;

/*
Содержит всю информацию о чанке:
    его terrain
    его изменения игроком.
 */


import main.java.com.pro100v1ad3000.world.WorldConfig;

public class Chunk {
    private final ChunkCoordinates coordinates;
    private final WorldConfig config;
    private final int[][] blocks; // Массив блоков (например, 16x16)

    public Chunk(ChunkCoordinates coordinates, WorldConfig config) {
        this.coordinates = coordinates;
        this.config = config;
        this.blocks = new int[32][32];
    }

    public void setBlock(int x, int y, int blockId) {
        blocks[x][y] = blockId;
    }

    public WorldConfig getConfig() {
        return config;
    }

    public int getBlock(int x, int y) {
        return blocks[x][y];
    }

    public ChunkCoordinates getCoordinates() { return coordinates; }

}


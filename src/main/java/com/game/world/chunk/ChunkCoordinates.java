package main.java.com.game.world.chunk;


/*
Вспомогательный класс для хранения координат чанка.
 */

public class ChunkCoordinates {
    private final int x;
    private final int y;

    public ChunkCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChunkCoordinates that = (ChunkCoordinates) obj;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}

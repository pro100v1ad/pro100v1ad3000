package main.java.com.game.world;

public class WorldConfig {
    private final String name;
    private final String seed;

    public WorldConfig(String name, String seed) {
        this.name = name;
        this.seed = seed;

    }

    // Геттеры
    public String getName() { return name; }
    public String getSeed() { return seed; }
}


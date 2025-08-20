package main.java.com.pro100v1ad3000.world;

public class WorldConfig {

    private String name;
    private String seed;

    public WorldConfig(String name, String seed) {
        this.name = name;
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    public String getSeed() {
        return seed;
    }
}

package main.java.com.pro100v1ad3000.world.generator;

import main.java.com.pro100v1ad3000.world.WorldConfig;

/*
Отвечает за генерацию самого мира:
    имеет метод для генерации чанка.
 */

public class WorldGenerator {


    public static WorldConfig createWorld(String name, String seed) {

        return new WorldConfig(name, seed);

    }

}



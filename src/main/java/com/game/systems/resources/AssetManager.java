package main.java.com.game.systems.resources;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    private static final Map<String, ResourceManager> resourceManagers  = new HashMap<>();;

    public static void addResourceManager(String key) {
        ResourceManager resourceManager = loadResourceManager(key);
        if(resourceManager != null) resourceManagers.put(key, resourceManager);
    }

    public static ResourceManager getResourceManager(String key) {
        return resourceManagers.get(key);
    }

    public static void unloadResourceManager(String key) {
        resourceManagers.remove(key);
    }

    public static ResourceManager loadResourceManager(String key) {
        ResourceManager resourceManager;

        switch (key) {
            case "startMenu": {
                resourceManager = new ResourceManager();
                resourceManager.loadSpriteSheet("sheet16x16", "");
                resourceManager.registerSprite("sprite16x16_0", "sheet16x16", 0, 16, 16);

                return resourceManager;
            }

        }

        return null;
    }

}

package main.java.com.pro100v1ad3000.systems.resources;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {

    private Map<String, ResourceManager> resourceManagers;

    public AssetManager() {
        resourceManagers = new HashMap<>();
    }

    public void addResourceManager(String key) {
        ResourceManager resourceManager = loadResourceManager(key);
        if(resourceManager != null) resourceManagers.put(key, resourceManager);
    }

    public ResourceManager getResourceManager(String key) {
        return resourceManagers.get(key);
    }

    public void unloadResourceManager(String key) {
        resourceManagers.remove(key);
    }

    public ResourceManager loadResourceManager(String key) {
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

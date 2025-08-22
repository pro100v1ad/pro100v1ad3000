package main.java.com.pro100v1ad3000.world;

/*
Все сводится к тому, что когда мир создан, через этот класс будет доступ к ChunkManager.
 */

import main.java.com.pro100v1ad3000.utils.Logger;
import main.java.com.pro100v1ad3000.world.chunk.ChunkManager;
import main.java.com.pro100v1ad3000.world.entities.players.LocalPlayer;
import main.java.com.pro100v1ad3000.world.entities.players.utils.PlayerInfoSaveManager;

public class World {

    private final WorldConfig config;
    private final ChunkManager chunkManager;
    private final LocalPlayer localPlayer;

    public World(WorldConfig config) {

        this.config = config;
        this.localPlayer = new LocalPlayer(PlayerInfoSaveManager.getInstance().getActivePlayer());
        this.chunkManager = new ChunkManager(this);

        Logger.info("The world is loaded...");

    }

    public void update() {

    }

    public ChunkManager getChunkManager() { return chunkManager; }
    public WorldConfig getConfig() { return config; }
    public LocalPlayer getLocalPlayer() { return localPlayer; }
}


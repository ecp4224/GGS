package net.mcforge.world;

import java.io.IOException;
import java.util.ArrayList;

import net.mcforge.server.Server;
import net.mcforge.system.ticker.Tick;
import net.mcforge.util.properties.Properties;

public interface Level {
    
    public void generateWorld(Generator g);
    
    public boolean isAutoSaveEnabled();
    
    public void setAutoSave(boolean set);
    
    public void loadProperties();
    
    public Properties getLevelProperties();
    
    public Block getTile(int x, int y, int z);
    
    public Block getTile(int i);
    
    public void setTile(Block b, int x, int y, int z, Server server);
    
    public void rawSetTile(int x, int y, int z, Block block, Server server, boolean addToTick);
    
    public void setNewSpawn(int WaterLevel);
    
    public void save() throws IOException;
    
    public void unload(Server server) throws IOException;
    
    public void unload(Server server, boolean save) throws IOException;
    
    
    /**
     * Load a level and return the level
     * @param filename
     *               The file to load and read
     * @return
     *        The level object
     * @throws IOException
     *                   An IOException is thrown if there is a problem reading the level
     */
    public void load(String filename, Server server) throws IOException;
    
    public PhysicsBlock addTick(int x, int y, int z, Block b, Server server);
    
    public void startPhysics(Server server);
    
    public int getLength();
    
    public int getWidth();
    
    public int getHeight();
    
    public int getDepth();
    
    public int getSpawnX();
    
    public int getSpawnY();
    
    public int getSpawnZ();
    
    public String getName();
    
    public void setWitdh(int width);
    
    public void setHeight(int height);
    
    public void setDepth(int depth);
    
    public void setSpawnX(int spawnx);
    
    public void setSpawnY(int spawny);
    
    public void setSpawnZ(int spawnz);
    
    public void setName(String name);
    
    public long getVersion();
    
    public ArrayList<Tick> getTicks();
}

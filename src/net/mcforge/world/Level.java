package net.mcforge.world;

import java.io.IOException;
import java.util.ArrayList;
import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;
import net.mcforge.system.ticker.Tick;
import net.mcforge.util.properties.Properties;
import net.mcforge.world.blocks.Block;
import net.mcforge.world.blocks.classicmodel.ClassicBlock;
import net.mcforge.world.blocks.classicmodel.PhysicsBlock;
import net.mcforge.world.exceptions.BackupFailedException;
import net.mcforge.world.generator.Generator;

public interface Level {
    
    /**
     * Generate a world
     * @param g
     *         The {@link Generator} object that will
     *         generate the world.
     */
    public void generateWorld(Generator<?> g);
    
    /**
     * Whether or not this level will autosave.
     * AutoSave will save the level every minute and save
     * when the level is unloaded.
     * @return
     *        True if the level will autosave, false if it will not.
     */
    public boolean isAutoSaveEnabled();
    
    /**
     * Returns whether or not this level has changed since the last time
     * it was saved.
     * @return
     *        True if the level has changed, false if it has not.
     */
    public boolean hasUpdated();
    
    /**
     * Set Whether the level will autosave or not.
     * AutoSave will save the level every minute and save
     * when the level is unloaded.
     * @param set
     *           True if the level will autosave, false if it will not.
     */
    public void setAutoSave(boolean set);
    
    /**
     * Load the properties for the level.
     */
    public void loadProperties();
    
    /**
     * The properties for this level
     * @return
     *        A {@link Properties} object for this level.
     */
    public Properties getLevelProperties();
    
    /**
     * Get a block at the X, Y, Z coordinates
     * @param x
     *        The X coordinate
     * @param y
     *        The Y coordinate
     * @param z
     *        The Z coordinate
     * @return
     *        The block at those coordinates
     */
    public Block getTile(int x, int y, int z);
    
    /**
     * Get a block in this level
     * @param index
     *            Which block to get
     * @return
     *        The block at that index
     */
    public Block getTile(int i);
    
    /**
     * Set a block in this world.
     * If the block is a physicsblock, it will be added
     * to the physics tick.
     * This method wont send out a change to the clients.
     * To do this, use {@link Player#GlobalBlockChange(short, short, short, ClassicBlock, ClassicLevel, Server)} instead
     * @param b
     *         The block to add
     * @param x
     *        The X coordinate
     * @param y
     *        The Y coordinate
     * @param z
     *        The Z coordinate
     * @param server
     *              The server this blockchange is happening in
     */
    public void setTile(Block b, int x, int y, int z, Server server);
    
    /**
     * Changes the block at the specified coordinates to the specified block
     * without checking for any physics changes
     * If you change a block in a level, it won't be sent to clients
     * 
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @param z - The z coordinate
     * @param block - The block to change to
     * @param server - The server
     * @param addtick - Whether this block should be added to the tick.
     */
    public void rawSetTile(int x, int y, int z, Block block, Server server, boolean addToTick);
    
    public void setNewSpawn(int WaterLevel);
    
    /**
     * Save the level
     * @throws IOException
     *                   An IOExceptoin is thrown if there is a problem writing to the file
     */
    public void save() throws IOException;
    
    /**
     * Unload this level.
     * All players who are in this level will be sent to the {@link Server#MainLevel}.
     * This level will save before unloading if {@link Level#isAutoSaveEnabled()} is set to true.
     * @param server
     *             The server thats unloading the level
     * @throws IOException
     *                   An IOException will occur if there is a problem saving the level
     */
    public void unload(Server server) throws IOException;
    
    /**
     * Unload this level.
     * All players who are in this level will be sent to the {@link Server#MainLevel}
     * @param server
     *             The server thats unloading the level
     * @param save
     *           Whether the level should save before unloading
     * @throws IOException
     *                   An IOException will occur if there is a problem saving the level
     */
    public void unload(Server server, boolean save) throws IOException;
    
    /**
     * This method will save the level, and copy the level file to a backup location
     * specified.
     * @param server
     *              The server making the backup.
     * @param location
     *                The location of the backup folder
     * @throws BackupFailedException
     *                              This exception is thrown if there was an IOException while backing up the level.
     */
    public void backup(Server server, String location) throws BackupFailedException;
    
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
    
    /**
     * Add a block to the Physics Tick
     * @param x
     *         The X cord. of the block
     * @param y
     *         The Y cord. of the block
     * @param z
     *         The Z cord. of the block
     * @param b
     *         The block to add to ticking
     * @param server
     *              The server the block belongs to
     * @return
     *        A copy of the PhysicsBlock added to the tick.
     */
    public PhysicsBlock addTick(int x, int y, int z, Block b, Server server);
    
    /**
     * Starts the level's physics ticker
     */
    public void startPhysics(Server server);
    
    /**
     * Get how many blocks this level can fit.
     * @return
     *       How many blocks this level can fit.
     */
    public int getLength();
    
    /**
     * Get how big the level is in width (x size).
     * @return
     *        The "x size" of the level.
     */
    public int getWidth();
    
    /**
     * Get how big the level is in height (y size).
     * @return
     *        The "y size" of the level.
     */
    public int getHeight();
    
    /**
     * Get how big the level is in depth (z size).
     * @return
     *        The "z size" of the level.
     */
    public int getDepth();
    
    /**
     * Get the x spawn coordinates (in blocks pos)
     * @return
     *        The x spawn
     */
    public double getSpawnX();
    
    /**
     * Get the y spawn coordinates (in blocks pos)
     * @return
     *        The y spawn
     */
    public double getSpawnY();
    
    /**
     * Get the z spawn coordinates (in block pos)
     * @return
     *        The z spawn
     */
    public double getSpawnZ();
    
    /**
     * Get the name of this level.
     * @return
     *        The name
     */
    public String getName();
    
    /**
     * Set the width of this level
     * @param width
     */
    public void setWitdh(int width);
    
    /**
     * Set the height of this level
     * @param height
     */
    public void setHeight(int height);
    
    /**
     * Set the depth of this level
     * @param depth
     */
    public void setDepth(int depth);
    
    /**
     * Set the x spawn coordinates
     * @param spawnx
     */
    public void setSpawnX(double spawnx);
    
    /**
     * Set the y spawn coordinates
     * @param spawny
     */
    public void setSpawnY(double spawny);
    
    /**
     * Set the z spawn coordinates
     * @param spawnz
     */
    public void setSpawnZ(double spawnz);
    
    /**
     * Set the name for this level
     * @param name
     */
    public void setName(String name);
    
    /**
     * Get the level save version of this level.
     * This data is usually saved first in the level file.
     * @return
     */
    public long getVersion();
    
    /**
     * Get an ArrayList of physics ticks currently active in this level.
     * @return
     */
    public ArrayList<Tick> getTicks();
}

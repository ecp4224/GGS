package net.mcforge.world.mcmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.mcforge.server.Server;
import net.mcforge.system.ticker.Tick;
import net.mcforge.util.properties.Properties;
import net.mcforge.world.Level;
import net.mcforge.world.blocks.Block;
import net.mcforge.world.blocks.classicmodel.PhysicsBlock;
import net.mcforge.world.blocks.mcmodel.SMPBlock;
import net.mcforge.world.exceptions.BackupFailedException;
import net.mcforge.world.generator.Generator;

public class MCLevel implements Level {
    
    private HashMap<ChunkPoint, Chunk> chunks = new HashMap<ChunkPoint, Chunk>();

    @Override
    public void generateWorld(Generator g) {
        
    }

    @Override
    public boolean isAutoSaveEnabled() {
        return false;
    }

    @Override
    public boolean hasUpdated() {
        return false;
    }

    @Override
    public void setAutoSave(boolean set) {
        
    }

    @Override
    public void loadProperties() {
        
    }

    @Override
    public Properties getLevelProperties() {
        return null;
    }

    @Override
    public Block getTile(int x, int y, int z) {
        ChunkPoint cp = ChunkPoint.toPoint(x, z);
        if (!chunks.containsKey(cp)) {
            //TODO Add to generate queue
            return null;
        }
        Chunk c = chunks.get(cp);
        int cx = x & 0xf;
        int cz = z & 0xf;
        return c.getTile(cx, y, cz);
    }

    @Override
    public Block getTile(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTile(Block b, int x, int y, int z, Server server) {
        SMPBlock block;
        if (!(b instanceof SMPBlock))
            return;
        block = (SMPBlock)b;
        ChunkPoint cp = ChunkPoint.toPoint(x, z);
        if (!chunks.containsKey(cp)) {
            //TODO Add to generate queue
            return;
        }
        Chunk c = chunks.get(cp);
        int cx = x & 0xf;
        int cz = z & 0xf;
        c.setTile(block, cx, y, cz);
    }

    @Override
    public void rawSetTile(int x, int y, int z, Block block, Server server, boolean addToTick) {
        setTile(block, x, y, z, server);
    }

    @Override
    public void setNewSpawn(int WaterLevel) {
        
    }

    @Override
    public void save() throws IOException {
        
    }

    @Override
    public void unload(Server server) throws IOException {
        
    }

    @Override
    public void unload(Server server, boolean save) throws IOException {
        
    }

    @Override
    public void backup(Server server, String location)
            throws BackupFailedException {
        
    }

    @Override
    public void load(String filename, Server server) throws IOException {
        
    }

    @Override
    public PhysicsBlock addTick(int x, int y, int z, Block b, Server server) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void startPhysics(Server server) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLength() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDepth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSpawnX() {
        return 0;
    }

    @Override
    public int getSpawnY() {
        return 0;
    }

    @Override
    public int getSpawnZ() {
        return 0;
    }

    @Override
    public String getName() {
        return "Test"; //TODO Name levels
    }

    @Override
    public void setWitdh(int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeight(int height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDepth(int depth) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSpawnX(int spawnx) {
        // TODO Set X Spawn
    }

    @Override
    public void setSpawnY(int spawny) {
        // TODO Set Y Spawn
    }

    @Override
    public void setSpawnZ(int spawnz) {
        // TODO Set Z Spawn
    }

    @Override
    public void setName(String name) {
        // TODO Set Name
    }

    @Override
    public long getVersion() {
        return 0;
    }

    @Override
    public ArrayList<Tick> getTicks() {
        throw new UnsupportedOperationException();
    }

}

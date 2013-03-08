package net.mcforge.world.mcmodel;

import java.io.IOException;
import java.security.InvalidParameterException;
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
import net.mcforge.world.generator.mcmodel.ChunkGenerator;

/**
 * A level that is represented by an infinite number of {@link Chunks}.
 * Each {@link Chunk} is 16x256x16.
 * @author MCForgeTeam
 *
 */
public class ChunkLevel implements Level {
    
    private HashMap<ChunkPoint, ChunkColumn> chunks = new HashMap<ChunkPoint, ChunkColumn>();
    protected Server owner;
    protected double spawnx;
    protected double spawny;
    protected double spawnz;
    protected float spawnyaw;
    protected float spawnpitch;
    protected String name;
    protected ChunkGeneratorService cgs;
    
    
    public ChunkLevel(Server server, String name) {
        this.owner = server;
        this.name = name;
        spawnx = 0.0;
        spawny = 255.0;
        spawnz = 0.0;
        cgs = new ChunkGeneratorService(this);
        server.getTicker().addTick(cgs);
    }
    
    /**
     * The server that owns this object
     * @return
     */
    public Server getServer() {
        return owner;
    }

    @Override
    public void generateWorld(Generator<?> g) {
        ChunkGenerator c;
        if (g instanceof ChunkGenerator)
            c = (ChunkGenerator)g;
        else
            throw new InvalidParameterException("You can only generate a chunk based level with a ChunkGenerator!");
        //TODO Find chunks to generate?
        //For now I guess assume to generate the first few chunks
        ChunkColumn[] tchunks = {
                new ChunkColumn(0, 0, this),
                new ChunkColumn(0, 1, this),
                new ChunkColumn(1, 0, this),
                new ChunkColumn(1, 1, this),
                new ChunkColumn(-1, 0, this),
                new ChunkColumn(-1, -1, this),
                new ChunkColumn(0, -1, this)
        };
        for (ChunkColumn cc : tchunks) {
            cc.generateFully(c);
            chunks.put(cc.getPoint(), cc);
        }
    }
    
    /*public void addChunk(Chunk c) {
        if (!chunks.containsKey(c.getPoint()))
            chunks.put(c.getPoint(), c);
    }*/

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
        ChunkColumn c = chunks.get(cp);
        int cx = x & 0xf;
        int cz = z & 0xf;
        return c.getBlock(cx, y, cz);
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
        ChunkColumn c = chunks.get(cp);
        int cx = x & 0xf;
        int cz = z & 0xf;
        c.setBlock(cz, cx, y, block);
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
    public double getSpawnX() {
        return spawnx;
    }

    @Override
    public double getSpawnY() {
        return spawny;
    }

    @Override
    public double getSpawnZ() {
        return spawnz;
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
    public void setSpawnX(double spawnx) {
        this.spawnx = spawnx;
    }

    @Override
    public void setSpawnY(double spawny) {
        this.spawny = spawny;
    }

    @Override
    public void setSpawnZ(double spawnz) {
        this.spawnz = spawnz;
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

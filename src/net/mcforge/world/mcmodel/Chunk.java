package net.mcforge.world.mcmodel;

import net.mcforge.world.Level;
import net.mcforge.world.blocks.Block;
import net.mcforge.world.blocks.mcmodel.SMPBlock;

public class Chunk {
    private SMPBlock[] blocks = new SMPBlock[16 * 16 * 256];
    private final int xlength = 16;
    private final int zlength = 16;
    private final int ylength = 256;
    private ChunkPoint point;
    private boolean updated;
    private Level owner;
    
    public Chunk(ChunkPoint point, Level owner) {
        this.point = point;
        this.owner = owner;
    }
    
    public Chunk(int x, int z, Level owner) {
        this(ChunkPoint.toPoint(x, z), owner);
    }
    
    public ChunkPoint getPoint() {
        return point;
    }
    
    /**
     * Place a block in this chunk.
     * @param block
     *             The block to place
     * @param x
     *         The X cord. <b>in this chunk. This value can't be greater than 16</b> 
     * @param y
     *         The Y cord. <b>in this chunk. This value can't be greater than 256</b>
     * @param z
     *         The Z cord. <b>in this chunk. This value can't be greator than 16</b>
     */
    public void setTile(SMPBlock block, int x, int y, int z) {
        setTile(block, posToInt(x, y, z));
    }
    
    /**
     * Get a block inside this chunk
     * @param x
     *         The X cord. <b>in this chunk. This value can't be greater than 16</b> 
     * @param y
     *         The Y cord. <b>in this chunk. This value can't be greater than 256</b>
     * @param z
     *         The Z cord. <b>in this chunk. This value can't be greator than 16</b>
     * @return
     */
    public Block getTile(int x, int y, int z) {
        return getTile(posToInt(x, y, z));
    }
    
    private Block getTile(int index) {
        if (index < 0) index = 0;
        if (index >= blocks.length) index = blocks.length - 1;
        return blocks[index];
    }
    
    private void setTile(SMPBlock block, int index) {
        if (index < 0) index = 0;
        if (index >= blocks.length) index = blocks.length - 1;
        blocks[index] = block;
        updated = true;
    }
    
    public Level getOwner() {
        return owner;
    }
    
    public void save() {
        //TODO Add to save queue
    }
    
    public boolean unload() {
        save();
        blocks = null;
        owner = null;
        return true;
    }
    
    private int posToInt(int x, int y, int z) {
        if (x < 0) { return -1; }
        if (x >= xlength) { return -1; }
        if (y < 0) { return -1; }
        if (y >= ylength) { return -1; }
        if (z < 0) { return -1; }
        if (z >= zlength) { return -1; }
        return x + z * xlength + y * ylength * zlength;
    }

    private int[] intToPos(int index) {
        int[] toreturn = new int[3];
        toreturn[1] = (index / xlength / ylength);
        index -= toreturn[1]*xlength*ylength;
        toreturn[2] = (index/xlength);
        index -= toreturn[2]*xlength;
        toreturn[0] = index;
        return toreturn;
    }
}

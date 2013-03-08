package net.mcforge.world.mcmodel;

import net.mcforge.server.Server;
import net.mcforge.world.Level;
import net.mcforge.world.blocks.mcmodel.SMPBlock;

public class Chunk {
    private SMPBlock[] blocks = new SMPBlock[16 * 16 * 16];
    private final int xlength = 16;
    private final int zlength = 16;
    private final int ylength = 16;
    private boolean updated;
    private ChunkColumn owner;
    private int y;
    public Chunk(ChunkColumn owner, int y) {
        this.owner = owner;
        this.y = y;
    }
    
    /**
     * Get the Y position of this chunk inside a {@link ChunkColumn}
     * @return
     */
    public int getY() {
        return y;
    }
    
    /**
     * Place a block in this chunk.
     * @param block
     *             The block to place
     * @param x
     *         The X cord. <b>in this chunk. This value can't be greater than 16</b> 
     * @param y
     *         The Y cord. <b>in this chunk. This value can't be greater than 16</b>
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
     *         The Y cord. <b>in this chunk. This value can't be greater than 16</b>
     * @param z
     *         The Z cord. <b>in this chunk. This value can't be greator than 16</b>
     * @return
     */
    public SMPBlock getTile(int x, int y, int z) {
        return getTile(posToInt(x, y, z));
    }
    
    private SMPBlock getTile(int index) {
        if (index < 0) index = 0;
        if (index >= blocks.length) index = blocks.length - 1;
        return blocks[index];
    }
    
    private void setTile(SMPBlock block, int index) {
        if (index < 0) index = 0;
        if (index >= blocks.length) index = blocks.length - 1;
        int[] pos = intToPos(index);
        SMPBlock wasthere = blocks[index];
        blocks[index] = block;
        if (wasthere != null) {
            if (wasthere.onDelete(owner.getOwner(), pos[0], pos[1], pos[2], getServer())) {
                blocks[index] = wasthere;
                return;
            }
        }
        if (block.onPlace(owner.getOwner(), pos[0], pos[1], pos[2], getServer())) {
            blocks[index] = wasthere;
            return;
        }
        updated = true;
    }
    
    /**
     * Convert the X, Y, Z position to a position in the world.
     * For example:
     * X-5 in the chunk may be X-21 in the world
     * @param x
     *        The X position in the chunk
     * @param y
     *        The Y position in the chunk
     * @param z
     *        The Z position in the chunk
     * @return
     *       An array of integers where index 0 is the X, index 1 is the Y, and index 2 is the Z.
     */
    public int[] getWorldPosition(int x, int y, int z) {
        int cx = getChunkOwner().getPoint().getX();
        int cz = getChunkOwner().getPoint().getZ();
        int truex = cx * 16 + x;
        int truez = cz * 16 + z;
        int truey = (getY() * 16) - (getY() - y);
        return new int[] { truex, truey, truez };
    }
    
    public Server getServer() {
        return owner.getOwner().getServer();
    }
    
    public ChunkColumn getChunkOwner() {
        return owner;
    }
    
    public Level getOwner() {
        return owner.getOwner();
    }
    
    public void save() {
        if (!updated)
            return;
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

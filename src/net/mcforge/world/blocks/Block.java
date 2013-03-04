package net.mcforge.world.blocks;

public interface Block {
    
    /**
     * Get the block ID the client will see
     * @return 
     *        The ID
     */
    public byte getVisibleBlock();
    
    public boolean canWalkThrough();
    
    public String getName();
}

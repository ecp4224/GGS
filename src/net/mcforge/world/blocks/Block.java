package net.mcforge.world.blocks;

import net.mcforge.server.Server;
import net.mcforge.world.Level;

public interface Block {
    
    /**
     * Get the block ID the client will see
     * @return 
     *        The ID
     */
    public byte getVisibleBlock();
    
    public boolean canWalkThrough();
    
    public String getName();
    
    /**
     * This method is called when the block is placed
     * @param l 
     *          The level the block was placed in
     * @param index 
     * @return
     *        True if you would like the action to be canceled. False if the action should continue.
     */
    public boolean onPlace(Level l, int x, int y, int z, Server server);
    
    /**
     * This method is called when the block is deleted
     * @param l 
     *          The level the block was deleted in
     * @param index 
     * @return
     *        True if you would like the action to be canceled. False if the action should continue.
     */
    public boolean onDelete(Level l, int x, int y, int z, Server server);
}

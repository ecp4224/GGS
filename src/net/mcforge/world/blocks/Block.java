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
    
    public void onPlace(Level l, int x, int y, int z, Server server);
    
    public void onDelete(Level l, int x, int y, int z, Server server);
}

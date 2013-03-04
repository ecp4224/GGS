package net.mcforge.world.blocks.mcmodel;

import net.mcforge.world.blocks.Block;

public abstract class SMPBlock implements Block {
    
    private String name;
    private byte ID;
    
    private static final SMPBlock[] blocks = {
        new Air(),
        new Dirt(),
        new Grass()
    };
    
    public SMPBlock(String name, byte ID) {
        this.ID = ID;
        this.name = name;
    }
    
    public SMPBlock() { }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public byte getVisibleBlock() {
        return ID;
    }
    
    /**
     * Get a block by ID
     * @param ID 
     *          The ID of the block to look for
     * @return 
     *        An empty version of the block with the basic information in it </br>
     *        If the block specified isn't found, then null is returned.
     */
    public static SMPBlock getBlock(byte ID) {
        for (SMPBlock b : blocks) {
            if (b.ID == ID)
                return b;
        }
        return null;
    }
    
    /**
     * Get a block by name
     * @param name 
     *            The name of the block to look for
     * @return 
     *        An empty version of the block with the basic information in it </br>
     *        If the block specified isn't found, then null is returned.
     */
    public static SMPBlock getBlock(String name) {
        for (SMPBlock b : blocks) {
            if (b.name.equalsIgnoreCase(name))
                return b;
        }
        return null;
    }
}

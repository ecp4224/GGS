package net.mcforge.world.blocks.mcmodel;

public class Air extends SMPBlock {

    @Override
    public boolean canWalkThrough() {
        return true;
    }
    
    public Air() {
        super("Air", (byte)0);
    }

}

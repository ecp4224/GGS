package net.mcforge.world.blocks.mcmodel;

public class Dirt extends SMPBlock {

    @Override
    public boolean canWalkThrough() {
        return false;
    }
    
    public Dirt() {
        super("Dirt", (byte)3);
    }

}

package net.mcforge.world.blocks.mcmodel;

public class Grass extends SMPBlock {

    @Override
    public boolean canWalkThrough() {
        return false;
    }
    
    public Grass() {
        super("Grass", (byte)2);
    }

}

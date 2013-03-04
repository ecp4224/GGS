package net.mcforge.world.mcmodel;

import net.mcforge.world.blocks.mcmodel.SMPBlock;

public class Chunk {
    private final SMPBlock[] blocks = new SMPBlock[16 * 16 * 256];
    private final int xwidth = 16;
    private final int ywidth = 16;
    private final int zwidth = 256;
}

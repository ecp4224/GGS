package net.mcforge.world.generator.mcmodel;

import net.mcforge.world.blocks.mcmodel.SMPBlock;
import net.mcforge.world.mcmodel.Chunk;

public class FlatGrassChunk implements ChunkGenerator {

    @Override
    public String getName() {
        return "FlatGrassChunk";
    }

    @Override
    public String[] getShortcuts() {
        return new String[0];
    }

    @Override
    public void generate(Chunk c) {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) {
                for (int z = 0; z < 16; z++) {
                    if (y > y / 2)
                        c.setTile(SMPBlock.getBlock("Air"), x, y, z);
                    else
                        c.setTile(SMPBlock.getBlock("Grass"), x, y, z);
                }
            }
        }
    }

}

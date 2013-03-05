package net.mcforge.world.generator.mcmodel;

import net.mcforge.world.generator.Generator;
import net.mcforge.world.mcmodel.Chunk;

public interface ChunkGenerator extends Generator<Chunk> {
    
    public void generate(Chunk c);
}

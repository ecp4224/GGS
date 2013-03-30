/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    if (c.getWorldPosition(x, y, z)[1] > 128)
                        c.setTile(SMPBlock.getBlock("Air"), x, y, z);
                    else
                        c.setTile(SMPBlock.getBlock("Grass"), x, y, z);
                }
            }
        }
    }
}

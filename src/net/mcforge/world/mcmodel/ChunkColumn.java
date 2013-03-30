/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.mcmodel;

import java.security.InvalidParameterException;

import net.mcforge.world.blocks.mcmodel.SMPBlock;
import net.mcforge.world.generator.mcmodel.ChunkGenerator;

public class ChunkColumn {
    private final Chunk[] chunks = new Chunk[16];
    private ChunkPoint point;
    private ChunkLevel owner;
    
    public ChunkColumn(ChunkLevel owner, ChunkPoint point) {
        this.point = point;
        this.owner = owner;
    }
    
    public ChunkColumn(int x, int z, ChunkLevel owner) {
        this(owner, new ChunkPoint(x, z));
    }
    
    public void addChunk(int y, Chunk c) {
        chunks[y] = c;
    }
    
    public void generateChunk(int y, ChunkGenerator g) {
        Chunk c = new Chunk(this, y);
        chunks[y] = c;
        owner.cgs.addChunk(c, g);
    }
    
    public void generateFully(ChunkGenerator g) {
        for (int y = 0; y < 16; y++) {
            generateChunk(y, g);
        }
    }
    
    /**
     * Modify a block in this chunk column
     * @param x
     *        The X position. This value can not be greater than 16.
     * @param y
     *        The Y position. This value can not be greater than 256
     * @param z
     *        The Z position. This value can not be greater than 16
     * @param block
     */
    public void setBlock(int x, int y, int z, SMPBlock block) {
        if (x > 16 || x < 0)
            throw new InvalidParameterException("The X value can't be greater than 16 or less than 0!");
        if (z > 16 || z < 0)
            throw new InvalidParameterException("The Z value can't be greater than 16 or less than 0!");
        if (y > 256 || y < 0)
            throw new InvalidParameterException("The Y value can't be greater than 256 or less than 0!");
        int cy = y >> 4;
        Chunk c = chunks[cy];
        if (c == null) {
            //TODO Generate
            return;
        }
        c.setTile(block, x, y & 0xf, z);
    }
    
    public SMPBlock getBlock(int x, int y, int z) {
        if (x > 16 || x < 0)
            throw new InvalidParameterException("The X value can't be greater than 16 or less than 0!");
        if (z > 16 || z < 0)
            throw new InvalidParameterException("The Z value can't be greater than 16 or less than 0!");
        if (y > 256 || y < 0)
            throw new InvalidParameterException("The Y value can't be greater than 256 or less than 0!");
        int cy = y >> 4;
        Chunk c = chunks[cy];
        if (c == null) {
            //TODO Generate
            return null;
        }
        return c.getTile(x, y & 0xf, z);
    }
    
    public ChunkPoint getPoint() {
        return point;
    }
    
    public ChunkLevel getOwner() {
        return owner;
    }
}

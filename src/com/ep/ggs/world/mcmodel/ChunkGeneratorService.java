/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.world.mcmodel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.ep.ggs.system.threading.Parallel;
import com.ep.ggs.system.threading.Parallel.ParallelRunner;
import com.ep.ggs.system.ticker.Tick;
import com.ep.ggs.world.generator.mcmodel.ChunkGenerator;


public class ChunkGeneratorService implements Tick {
    protected List<CData> chunk_queue = Collections.synchronizedList(new LinkedList<CData>());
    protected ChunkLevel owner;
    
    public ChunkGeneratorService(ChunkLevel owner) {
        this.owner = owner;
    }
    
    public ChunkLevel getOwner() {
        return owner;
    }
    
    public void generateChunks() {
        Parallel.parallelFor(chunk_queue, new ParallelRunner<CData>() {
            @Override
            public void run(CData data, int index) {
                data.gen.generate(data.c);
                owner.getServer().log("Generated chunk @ " + data.c.getChunkOwner().getPoint() + " Y: " + data.c.getY() + " Thread: " + Thread.currentThread().getId() + " Air?: " + data.c.isChunkAir(), true);
                data.c.getChunkOwner().addChunk(data.c.getY(), data.c);
                chunk_queue.remove(data);
            }
        });
    }
    
    public boolean addChunk(Chunk c) {
        return false;
    }
    
    public boolean addChunk(Chunk c, ChunkGenerator gen) {
        CData cc = new CData();
        cc.gen = gen;
        cc.c = c;
        chunk_queue.add(cc);
        return true;
    }
    
    private class CData {
        ChunkGenerator gen;
        Chunk c;
    }

    @Override
    public void tick() {
        while (!chunk_queue.isEmpty()) {
            owner.getServer().log("Requesting generate.");
            generateChunks();
        }
    }

    @Override
    public boolean inSeperateThread() {
        return true;
    }

    @Override
    public int getTimeout() {
        return 5; //TODO Find a good timeout..
    }

    @Override
    public String tickName() {
        return owner.getName() + "ChunkService";
    }
}

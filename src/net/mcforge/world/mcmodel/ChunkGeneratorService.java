package net.mcforge.world.mcmodel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.mcforge.system.threading.Parallel;
import net.mcforge.system.threading.Parallel.ParallelRunner;
import net.mcforge.system.ticker.Tick;
import net.mcforge.world.generator.mcmodel.ChunkGenerator;

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
                owner.getServer().Log("Generated chunk @ " + data.c.getChunkOwner().getPoint() + " Y: " + data.c.getY() + " Thread: " + Thread.currentThread().getId());
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
            owner.getServer().Log("Requesting generate.");
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

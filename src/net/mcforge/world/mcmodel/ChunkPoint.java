package net.mcforge.world.mcmodel;

public class ChunkPoint {
    
    private int x;
    private int z;
    
    public ChunkPoint(int x, int z) {
        this.x = x;
        this.z = z;
    }
    
    public int getX() {
        return x;
    }
    
    public int getZ() {
        return z;
    }
    
    public static ChunkPoint toPoint(int x, int y, int z) {
        return new ChunkPoint(x >> 4, z >> 4);
    }
    
    public static ChunkPoint toPoint(int x, int z) {
        return new ChunkPoint(x >> 4, z >> 4);
    }
    
    @Override
    public boolean equals(Object point) {
        if (point instanceof ChunkPoint) {
            ChunkPoint cp = (ChunkPoint)point;
            return cp.x == x && cp.z == z;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return (x + ":" + z).hashCode();
    }
}

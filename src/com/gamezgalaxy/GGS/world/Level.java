package com.gamezgalaxy.GGS.world;

import java.io.IOException;
import com.gamezgalaxy.GGS.world.convert.DatToGGS;

public class Level {
	
	Block[] blocks;
	
	public int width;
	
	public int height;
	
	public int depth;
	
	public Level(int width, int height, int depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	public void setTile(Block b, int index) {
		if (index < 0) index = 0;
		if (index >= blocks.length) index = blocks.length - 1;
		blocks[index] = b;
	}
	
	public void setTile(Block b, int x, int y, int z) {
		setTile(b, PosToInt(x, y, z));
	}
	
	public int PosToInt(int x, int z, int y) {
        if (x < 0) { return -1; }
        if (x >= width) { return -1; }
        if (y < 0) { return -1; }
        if (y >= height) { return -1; }
        if (z < 0) { return -1; }
        if (z >= depth) { return -1; }
        return x + z * width + y * width * depth;
    }
	
	public static Level Convert(String file) throws IOException {
		DatToGGS newlvl = new DatToGGS();
		newlvl.load(file);
		Level lvl = new Level(newlvl.level.width, newlvl.level.height, newlvl.level.depth);
		lvl.blocks = new Block[lvl.width*lvl.height*lvl.depth];
		int[] cords = new int[3];
		for (int i = 0; i < lvl.blocks.length; i++) {
			cords = newlvl.getCoords(i);
			lvl.blocks[lvl.PosToInt(cords[0], cords[1], cords[2])] = Block.getBlock(newlvl.level.blocks[i]);
		}
		return lvl;
	}

}

/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.world;

import java.io.IOException;
import com.gamezgalaxy.GGS.world.convert.DatToGGS;

public class Level {
	
	Block[] blocks;
	
	public short width;
	
	public short height;
	
	public short depth;
	
	public Level(short width, short height, short depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		blocks = new Block[width*height*depth];
	}
	
	public void FlatGrass() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < depth; z++) {
					if (y < height / 2)
						blocks[PosToInt(x, y, z)] = Block.getBlock("dirt");
					else if (y == height / 2)
						blocks[PosToInt(x, y, z)] = Block.getBlock("grass");
					else
						blocks[PosToInt(x, y, z)] = Block.getBlock("air");
				}
			}
		}
	}
	
	public void setTile(Block b, int index) {
		if (index < 0) index = 0;
		if (index >= blocks.length) index = blocks.length - 1;
		blocks[index] = b;
	}
	
	public Block getTile(int index) {
		if (index < 0) index = 0;
		if (index >= blocks.length) index = blocks.length - 1;
		if (blocks[index].name.equals("NULL"))
			System.out.println("" + blocks[index].getVisableBlock());
		return blocks[index];
	}
	
	public Block getTile(int x, int y, int z) {
		return getTile(PosToInt(x, y, z));
	}
	
	public int getLength() {
		return blocks.length;
	}
	
	public void setTile(Block b, int x, int y, int z) {
		setTile(b, PosToInt(x, y, z));
	}
	
	public int PosToInt(int x, int y, int z) {
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
		Level lvl = new Level((short)newlvl.level.width, (short)newlvl.level.height, (short)newlvl.level.depth);
		lvl.blocks = new Block[lvl.width*lvl.height*lvl.depth];
		int[] cords = new int[3];
		for (int i = 0; i < lvl.blocks.length; i++) {
			cords = newlvl.getCoords(i);
			lvl.blocks[lvl.PosToInt(cords[0], cords[1], cords[2])] = Block.getBlock(newlvl.level.blocks[i]);
		}
		return lvl;
	}

}

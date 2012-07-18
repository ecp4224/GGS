package com.gamezgalaxy.GGS.world;

import com.gamezgalaxy.GGS.world.blocks.Block;

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

}

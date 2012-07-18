package com.gamezgalaxy.GGS.world;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.world.blocks.Block;
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
	
	public static Level Convert(String file) throws IOException {
		DatToGGS newlvl = new DatToGGS();
		newlvl.load(file);
		Level lvl = new Level(newlvl.level.width, newlvl.level.height, newlvl.level.depth);
		//TODO Convert all blocks
		return lvl;
	}

}

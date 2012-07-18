package com.gamezgalaxy.GGS.world.convert;

import java.io.*;
import java.text.DateFormat;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;

public class DatToGGS {
	public com.mojang.minecraft.level.Level level = null;
	private int width;
	private int height;

	public DatToGGS() {
	}

	// load from file called filename
	public void load(String filename) {
		FileInputStream fis = null;
		GZIPInputStream gzis = null;
		ObjectInputStream in = null;
		DataInputStream inputstream = null;
		try {
			fis = new FileInputStream(filename);
			gzis = new GZIPInputStream(fis);
			inputstream = new DataInputStream(gzis);
			if((inputstream.readInt()) != 0x271bb788) {
				return;
			}
			if((inputstream.readByte()) > 2) {
				System.out.println("Error: Level version > 2, this is unexpected!");
				return;
			}
			in = new ObjectInputStream(gzis);
			level = (com.mojang.minecraft.level.Level)in.readObject();
			inputstream.close();
			in.close();
			System.out.println("Loading level "+filename+" successful");
		} catch(IOException ex) {
			ex.printStackTrace();
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		level.initTransient();
	}

	// save in file called filename
	public void save(String filename) {
		FileOutputStream fos = null;
		GZIPOutputStream gzos = null;
		ObjectOutputStream out = null;
		DataOutputStream outputstream = null;
		try {
			fos = new FileOutputStream(filename);
			gzos = new GZIPOutputStream(fos);
			outputstream = new DataOutputStream(gzos);
			outputstream.writeInt(0x271bb788);
			outputstream.writeByte(2);
			out = new ObjectOutputStream(gzos);
			out.writeObject(level);
			outputstream.close();
			out.close();
			System.out.println("Saving level "+filename+" successful");
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	// prints all there is to know about a level, except for the blocks data
	public void printInfo() {
		if (level == null) {
			return;
		}
		System.out.println("Level info:");
		System.out.println("name: "+level.name);
		System.out.println("creator: "+level.creator);
		System.out.println("createTime: "+(DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL).format(level.createTime)));
		System.out.println("width: "+level.width);
		System.out.println("height: "+level.height);
		System.out.println("depth: "+level.depth);
		System.out.println("spawnpoint: ["+level.xSpawn+","+level.ySpawn+","+level.zSpawn+"]");
		System.out.println("spawn rotation: "+level.rotSpawn);
	}

	// safe to use method, return value let's you know if anything was changed
	public boolean setTile(int x, int y, int z, int t) {
		if (
			x >=0 && x < level.width &&
			y >=0 && y < level.depth &&
			z >=0 && z < level.height &&
			t >= 0 && t <= 37
		) {
			if (t == 8 || t == 10) {
				level.setTile(x,y,z,t);
			} else if (t >= 0 && t <= 18) {
				level.setTileNoUpdate(x,y,z,t);
			}
			return true;
		}
		return false;
	}

	// gets you the level coordinates from the blocks array index
	public int[] getCoords(int index) {
		int x = index % width;
		index = (index-x) / width;
		int z = index % height;
		int y = (index-z) / height;
		return new int[] {x, y, z};
	}

	public void clearBlocks() {
		for (int i=0; i<level.blocks.length; i++) {
			level.blocks[i] = 0;
		}
	}

	public void floor(int y, int type) {
		for (int i=0; i<level.width; i++) {
		for (int j=0; j<level.height; j++) {
			setTile(i,y,j,type);
		}
		}
	}

	public void wallX(int x1, int x2, int z, int y, int height, int type) {
		for (int i=x1; i<=x2; i++) {
		for (int j=y; j<y+height; j++) {
			if (!setTile(i,j,z,type)) {
				System.out.println("Warning: a tile got ignored while building a wallX: ["+i+","+j+","+z+"]");
			}
		}
		}
	}

	public void wallZ(int x, int z1, int z2, int y, int height, int type) {
		for (int i=z1; i<=z2; i++) {
		for (int j=y; j<y+height; j++) {
			if (!setTile(x,j,i,type)) {
				System.out.println("Warning: a tile got ignored while building a wallZ: ["+x+","+j+","+i+"]");
			}
		}
		}
	}

	// substitute all of block type 'from' to 'to' :) returning the number of blocks altered
	public int substitute(byte from, byte to) {
		int count=0;
		for (int i=0; i<level.blocks.length; i++) {
			if (level.blocks[i] == from) {
				level.blocks[i] = to;
				count++;
			}
		}
		return count;
	}

	public void setSize(int x, int y, int z) {
		level.setData(x, y, z, new byte[x*y*z]);
	}

	public static void main(String [] args) {
		DatToGGS le = new DatToGGS();
		String filename = "server_level.dat";
		if(args.length > 0) {
			filename = args[0];
			le.load(filename);
			if (le.level == null) {
				System.out.println("Loading level "+filename+" failed");
				return;
			}
		} else {
			le.level = new com.mojang.minecraft.level.Level();
		}
		// Do some fancy editing here

		// set a custom size: 256 wide, 128 high, 512 length
		le.setSize(256, 128, 512);

		// first, let's clear out the field
		le.clearBlocks();

		// add a bunch of walls to fill the lower part of the level
		for (int i=0; i<le.level.width; i++) {
			le.wallX(0,le.level.height-1,i,0,(int)le.level.getWaterLevel()-1,3);
		}
		// lay the floor
		le.floor((int)le.level.getWaterLevel()-1,2);

		// let the level find a spawn location for us
		le.level.findSpawn();

		// Leave our fingerprint
		le.level.creator = "Minecrafter";
		le.level.name = "A Custom World";
		le.level.createTime = System.currentTimeMillis();

		le.save(filename);
		le.printInfo();
	}
}

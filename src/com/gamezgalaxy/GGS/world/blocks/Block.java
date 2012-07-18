package com.gamezgalaxy.GGS.world.blocks;

import com.gamezgalaxy.GGS.world.Level;

public abstract class Block {
	
	static Block[] blocks = new Block[] {
			new Air()
			//TODO Add all blocks here
	};
	
	public byte ID;
	
	public String name;
	
	public void onPlace(Level l) {
		
	}
	
	public void onDelete(Level l) {
		
	}
	
	public Block(byte ID, String name) {
		this.ID = ID;
		this.name = name;
	}
	
	public static Block getBlock(byte ID) {
		for (Block b : blocks) {
			if (b.ID == ID)
				return b;
		}
		return null;
	}
	
	public static Block getBlock(String name) {
		for (Block b : blocks) {
			if (b.name.equalsIgnoreCase(name))
				return b;
		}
		return null;
	}

}

package com.gamezgalaxy.GGS.world.blocks;

import com.gamezgalaxy.GGS.world.Level;

public abstract class Block {
	
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

}

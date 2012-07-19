package com.gamezgalaxy.GGS.world;

import com.gamezgalaxy.GGS.world.blocks.*;


public abstract class Block {
	
	static Block[] blocks = new Block[] {
			new Air(),
			new Aqua(),
			new Bedrock(),
			new Black(),
			new Blue(),
			new Bookshelf(),
			new Brick(),
			new BrownShroom(),
			new CoalOre(),
			new Cobblestone(),
			new Cyan(),
			new Dirt(),
			new DoubleStair(),
			new Glass(),
			new GoldBlock(),
			new GoldOre(),
			new Grass(),
			new Gravel(),
			new Gray(),
			new Green(),
			new Indigo(),
			new IronBlock(),
			new IronOre(),
			new Lava(),
			new Leaves(),
			new Lime(),
			new MossyCobblestone(),
			new Obsidian(),
			new Orange(),
			new Pink(),
			new Purple(),
			new Red(),
			new RedFlower(),
			new RedShroom(),
			new Sand(),
			new Sapling(),
			new Sponge(),
			new Stair(),
			new StationaryLava(),
			new StationaryWater(),
			new Stone(),
			new TNT(),
			new Unknown(),
			new Violet(),
			new Water(),
			new White(),
			new Wood(),
			new WoodenPlank(),
			new Yellow(),
			new YellowFlower()
	};
	
	public byte ID;
	
	public String name;
	
	public void onPlace(Level l) {
		
	}
	
	public byte getVisableBlock() {
		return ID;
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
		return new Unknown();
	}
	
	public static Block getBlock(String name) {
		for (Block b : blocks) {
			if (b.name.equalsIgnoreCase(name))
				return b;
		}
		return new Unknown();
	}

}

/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.world.blocks;

import net.mcforge.server.Server;
import net.mcforge.world.Block;
import net.mcforge.world.Level;

public class Grass extends Block {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Grass(byte ID, String name) {
		super(ID, name);
	}
	
	public Grass() {
		super((byte)2, "Grass");
	}
	
	@Override
	public void onPlace(Level l, int x, int y, int z, Server server) {
		if (l.getTile(x, y - 1, z) == Block.getBlock("Grass"))
			l.setTile(Block.getBlock("Dirt"), x, y - 1, z, server);
	}
}


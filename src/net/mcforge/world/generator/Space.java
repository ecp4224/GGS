/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.world.generator;

import java.util.Random;

import net.mcforge.server.Server;
import net.mcforge.world.Block;
import net.mcforge.world.Generator;
import net.mcforge.world.Level;

public class Space implements Generator {

	private Server _server;
	
	/**
	 * The constructor for the space level generator
	 * 
	 * @param server - The server the level is in
	 */
	public Space(Server server) {
		this._server = server;
	}
	@Override
	public void generate(Level l) {
		Random rand = new Random(System.currentTimeMillis());
		for (int x = 0; x < l.width; x++) {
			for (int y = 0; y < l.height; y++) {
				for (int z = 0; z < l.depth; z++) {
					if (y == 0)
						l.skipChange(x, y, z, Block.getBlock("Bedrock"), _server);
					else if (x == 0 || x == l.width - 1 || z == 0 || z == l.depth - 1 || y == 1 || y == l.height - 1)
						l.skipChange(x, y, z, Block.getBlock( rand.nextInt(100) == 0 ? "IronOre" : "Obsidian"), _server);
				}
			}
		}
	}
	@Override
	public void generate(Level l, int x, int y, int z) {
		generate(l);
	}

}


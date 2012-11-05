/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.world.generator;

import net.mcforge.server.Server;
import net.mcforge.world.Block;
import net.mcforge.world.Generator;
import net.mcforge.world.Level;

public class FlatGrass implements Generator {

	private Server _server;
	
	/**
	 * The constructor for the flatgrass level generator
	 * @param server
	 *              The server the level is in
	 */
	public FlatGrass(Server server) {
		this._server = server;
	}
	@Override
	public void generate(Level l) {
		for (int x = 0; x < l.width; x++) {
			for (int y = 0; y < l.height; y++) {
				for (int z = 0; z < l.depth; z++) {
					if (y < l.height / 2)
						l.skipChange(x, y, z,Block.getBlock("dirt"),  _server);
					else if (y == l.height / 2)
						l.skipChange(x, y, z,Block.getBlock("grass"),  _server);
					else
						l.skipChange( x, y, z,Block.getBlock("air"),  _server);
				}
			}
		}
	}
	@Override
	public void generate(Level l, int x, int y, int z) {
		generate(l);
	}

}


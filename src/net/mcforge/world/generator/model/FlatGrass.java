/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.generator.model;

import net.mcforge.server.Server;
import net.mcforge.world.blocks.Block;
import net.mcforge.world.generator.Generator;
import net.mcforge.world.Level;

/**
 * A generator that creates a flat grass area, perfect for freebuild!
 * @author MCForgeTeam
 *
 */
public class FlatGrass implements Generator {

    private Server _server;
    @Override
    public String getName() {
    	return "FlatGrass";
    }
	@Override
	public String[] getShortcuts() {
		return new String[] { "Flat" };
	}
    
    /**
     * The constructor for the flatgrass level generator
     * @param server
     *              The server the level is in
     */
    public FlatGrass(Server server) {
        this._server = server;
    }
    @Override
    public void generate(final Level l) {
        for (int x = 0; x < l.getWidth(); x++) {
            for (int y = 0; y < l.getHeight(); y++) {
                for (int z = 0; z < l.getDepth(); z++) {
                    if (y < l.getHeight() / 2)
                        l.rawSetTile(x, y, z,Block.getBlock("dirt"),  _server, false);
                    else if (y == l.getHeight() / 2)
                        l.rawSetTile(x, y, z,Block.getBlock("grass"),  _server, false);
                    else
                        l.rawSetTile( x, y, z,Block.getBlock("air"),  _server, false);
                }
            }
        }
    }
    @Override
    public void generate(Level l, int x, int y, int z) {
        generate(l);
    }

}


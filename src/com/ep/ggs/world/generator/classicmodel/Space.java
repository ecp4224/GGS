/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.world.generator.classicmodel;

import java.util.Random;

import com.ep.ggs.server.Server;
import com.ep.ggs.world.blocks.classicmodel.ClassicBlock;
import com.ep.ggs.world.classicmodel.ClassicLevel;


/**
 * Creates a Space level where the walls are made of Bedrock.
 * @author MCForge Team
 *
 */
public class Space implements ClassicGenerator {

    private Server _server;
    
    @Override
    public String getName() {
    	return "Space";
    }
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}
    
    /**
     * The constructor for the space level generator
     * 
     * @param server - The server the level is in
     */
    public Space(Server server) {
        this._server = server;
    }
    @Override
    public void generate(ClassicLevel l) {
        Random rand = new Random(System.currentTimeMillis());
        for (int x = 0; x < l.getWidth(); x++) {
            for (int y = 0; y < l.getHeight(); y++) {
                for (int z = 0; z < l.getDepth(); z++) {
                    if (y == 0)
                        l.rawSetTile(x, y, z, ClassicBlock.getBlock("Bedrock"), _server, false);
                    else if (x == 0 || x == l.getWidth() - 1 || z == 0 || z == l.getDepth() - 1 || y == 1 || y == l.getHeight() - 1)
                        l.rawSetTile(x, y, z, ClassicBlock.getBlock( rand.nextInt(100) == 0 ? "IronOre" : "Obsidian"), _server, false);
                }
            }
        }
    }
    @Override
    public void generate(ClassicLevel l, int x, int y, int z) {
        generate(l);
    }

}

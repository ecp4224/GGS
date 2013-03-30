/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.blocks.classicmodel;

import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;
import net.mcforge.world.Level;


public class Stair extends ClassicBlock {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Stair(byte ID, String name) {
        super(ID, name);
    }
    
    public Stair() {
        super((byte)44, "Stair");
    }
    
    @Override
    public boolean onPlace(Level l, int x, int y, int z, Server server) {
        if (l.getTile(x, y - 1, z).getName().equals("Stair")) {
            Player.GlobalBlockChange((short)x, (short)y, (short)z, ClassicBlock.getBlock("Air"), l, server);
            Player.GlobalBlockChange((short)x, (short)(y - 1), (short)z, ClassicBlock.getBlock("DoubleStair"), l, server);
            return true;
        }
        return false;
    }

}


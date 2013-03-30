/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.blocks.classicmodel;


public class StationaryWater extends ClassicBlock {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public StationaryWater(byte ID, String name) {
        super(ID, name);
    }
    
    public StationaryWater() {
        super((byte)9, "StationaryWater");
    }
    
    @Override
    public boolean canWalkThrough() {
        return true;
    }

}


/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.world.blocks.classicmodel;


public class Sapling extends ClassicBlock {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Sapling(byte ID, String name) {
        super(ID, name);
    }
    
    public Sapling() {
        super((byte)6, "Sapling");
    }
    
    @Override
    public boolean canWalkThrough() {
        return true;
    }

}


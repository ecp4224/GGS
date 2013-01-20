/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.blocks.classicmodel;

import net.mcforge.world.blocks.Block;

public class Cyan extends Block {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Cyan(byte ID, String name) {
        super(ID, name);
    }
    
    public Cyan() {
        super((byte)27, "Cyan");
    }

}


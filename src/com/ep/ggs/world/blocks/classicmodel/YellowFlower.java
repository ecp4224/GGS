/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.world.blocks.classicmodel;

import com.ep.ggs.server.Server;

public class YellowFlower extends RedFlower {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public YellowFlower(byte ID, String name) {
        super(ID, name);
    }
    
    public YellowFlower() {
        super((byte)37, "YellowFlower");
    }
    
    public YellowFlower(Server s) {
        super((byte)37, "YellowFlower", s);
        
    }

    @Override
    public PhysicsBlock clone(Server s) {
        return new YellowFlower(s);
    }
    
    @Override
    public boolean canWalkThrough() {
        return true;
    }

}


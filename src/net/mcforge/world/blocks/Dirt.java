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
import net.mcforge.world.PhysicsBlock;

public class Dirt extends PhysicsBlock {

    private int wait;
    private static final long serialVersionUID = 1L;
    public Dirt(byte ID, String name) {
        this(ID, name, null);
    }
    
    public Dirt() {
        this((byte)3, "Dirt");
    }
    
    public Dirt(Server s) {
        this((byte)3, "Dirt", s);
    }
    
    public Dirt(byte ID, String name, Server s) {
        super((byte)3, "Dirt", s);
    }

    @Override
    public PhysicsBlock clone(Server s) {
        Dirt d = new Dirt(s);
        d.wait = wait;
        return d;
    }

    @Override
    public void tick() {
        if (wait < 30) {
            wait++;
            return;
        }
        wait = 0;
        if (change())
            super.change(Block.getBlock("Grass"));
        else
            super.stopTick();
    }
    
    public boolean change() {
        if (getLevel() == null) 
            return false;
        if (!getLevel().getTile(getX(), getY() + 1, getZ()).canWalkThrough())
            return false;
        int y = getY() + 1;
        for (; y < getLevel().getHeight() - 1; y++) {
            if (!getLevel().getTile(getX(), y + 1, getZ()).canWalkThrough()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean initAtStart() {
        return false;
    }

    @Override
    public boolean inSeperateThread() {
        return false;
    }

    @Override
    public int getTimeout() {
        return 30;
    }

}


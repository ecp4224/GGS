/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.blocks.classicmodel;

import net.mcforge.server.Server;
import net.mcforge.world.blocks.PhysicsBlock;

public class Gravel extends Sand {

    int wait = 0;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public Gravel(byte ID, String name) {
        super(ID, name);
    }

    public Gravel() {
        super((byte)13, "Gravel");
    }

    public Gravel(byte b, String string, Server s) {
        super(b, string, s);
    }

    @Override
    public PhysicsBlock clone(Server s) {
        Gravel ss = new Gravel((byte)13, "Gravel", s);
        ss.wait = wait;
        return ss;
    }

}


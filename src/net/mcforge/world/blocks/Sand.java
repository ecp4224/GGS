/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.blocks;

import net.mcforge.server.Server;
import net.mcforge.world.PhysicsBlock;

public class Sand extends PhysicsBlock {

	int wait = 0;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sand(byte ID, String name) {
		super(ID, name);
	}
	
	public Sand() {
		super((byte)12, "Sand");
	}
	
	public Sand(byte b, String string, Server s) {
		super(b, string, s);
	}

	@Override
	public PhysicsBlock clone(Server s) {
		Sand ss = new Sand((byte)12, "Sand", s);
		ss.wait = wait;
		return ss;
	}

	@Override
	public void tick() {
		if (wait <= 3) {
			wait++;
			return;
		}
		if (getLevel().getTile(getX(), getY() - 1, getZ()).getVisableBlock() == 0)
			move(getX(), getY() - 1, getZ());
	}

}

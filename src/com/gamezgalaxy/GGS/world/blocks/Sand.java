/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.world.blocks;

import com.gamezgalaxy.GGS.server.Server;
import com.gamezgalaxy.GGS.world.Block;
import com.gamezgalaxy.GGS.world.PhysicsBlock;

public class Sand extends PhysicsBlock {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Sand(byte ID, String name) {
		super(ID, name);
		// TODO Auto-generated constructor stub
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
		return ss;
	}

	@Override
	public void Tick() {
		if (getLevel().getTile(getX(), getY() - 1, getZ()) == Block.getBlock((byte)0))
			move(getX(), getY() - 1, getZ());
	}

}

/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.world.blocks;

import com.gamezgalaxy.GGS.server.Server;
import com.gamezgalaxy.GGS.world.PhysicsBlock;

public class Lava extends PhysicsBlock {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Lava(byte ID, String name) {
		super(ID, name);
	}
	
	public Lava() {
		super((byte)10, "Lava");
	}

	public Lava(byte b, String string, Server s) {
		super(b, string, s);
	}

	@Override
	public PhysicsBlock clone(Server s) {
		Lava l = new Lava((byte)10, "Lava", s);
		return l;
	}

	@Override
	public void Tick() {
		//TODO Add physics for lava
	}

}

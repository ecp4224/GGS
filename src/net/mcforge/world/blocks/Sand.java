/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.blocks;

import java.util.ArrayList;
import java.util.Random;

import net.mcforge.server.Server;
import net.mcforge.world.Block;
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
		if (getLevel().getTile(getX(), getY() - 1, getZ()).getVisableBlock() == 0)
        {
			super.move(getX(), getY() - 1, getZ());
            return;
        }
		if (getServer().newSand) {
			boolean north = getLevel().getTile(getX() + 1, getY(), getZ()).getVisableBlock() == 0;
			boolean south = getLevel().getTile(getX() - 1, getY(), getZ()).getVisableBlock() == 0;
			boolean east = getLevel().getTile(getX(), getY(), getZ() + 1).getVisableBlock() == 0;
			boolean west = getLevel().getTile(getX(), getY(), getZ() - 1).getVisableBlock() == 0;
			if (!north && !south && !east && !west)
				return;
			ArrayList<Vector2D> card = new ArrayList<Vector2D>();
			if (north)
				card.add(new Vector2D(1, 0));
			if (south)
				card.add(new Vector2D(-1, 0));
			if (east)
				card.add(new Vector2D(0, 1));
			if (west)
				card.add(new Vector2D(0, -1));
			ArrayList<Vector2D> diag = new ArrayList<Vector2D>();
			if (north && east)
				diag.add(new Vector2D(1, 1));
			if (south && east)
				diag.add(new Vector2D(-1, 1));
			if (north && west)
				diag.add(new Vector2D(1, -1));
			if (south && west)
				diag.add(new Vector2D(-1, -1));
			ArrayList<Vector2D> check = new ArrayList<Vector2D>();
			while (card.size() > 0)
			{
				int i = new Random().nextInt(card.size());
				check.add(card.get(i));
				card.remove(i);
			}
			while (diag.size() > 0)
			{
				int i = new Random().nextInt(diag.size());
				check.add(diag.get(i));
				diag.remove(i);
			}

			for (int i = 0; i < check.size(); ++i)
			{
				int x = (int)check.get(i).x;
				int z = (int)check.get(i).z;
				int y = x * z == 0 ? 1 : 2;
				if (getLevel().getTile(getX() + x, getY() - y, getZ() + z).getVisableBlock() == 0 && getLevel().getTile(getX() + x, getY() - y + 1, getZ() + z).getVisableBlock() == 0)
				{
					super.move(getX() + x, getY() - y, getZ() + z);
					check.clear();
					diag.clear();
					card.clear();
					return;
				}
			}
			check.clear();
			diag.clear();
			card.clear();
		}
		else
			super.stopTick();
	}
	
	private class Vector2D {
		private int x;
		private int z;
		public Vector2D(int x, int z) {
			this.x = x;
			this.z = z;
		}
	}
}

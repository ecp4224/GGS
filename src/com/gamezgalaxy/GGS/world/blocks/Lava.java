/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.world.blocks;

import java.util.Random;

import com.gamezgalaxy.GGS.server.Server;
import com.gamezgalaxy.GGS.world.Block;
import com.gamezgalaxy.GGS.world.PhysicsBlock;

public class Lava extends PhysicsBlock {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int time;
	
	private int type;
	
	/**
	 * 1 = Meaning the block didnt update
	 * 2 = The block did update
	 * 3 = Ignore this block
	 * 0 = Source block
	 */
	private int update;
	
	private static int wait;
	
	private final Random random = new Random();

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
		l.type = type;
		if (l.type == 0)
			l.type = random.nextInt(6) + 1;
		l.type = 4; //TODO REMOVE
		return l;
	}

	@Override
	public void Tick() {
		if (!nearSponge(getLevel().PosToInt(getX(), getY(), getZ()))) {
			if (time < random.nextInt(6)) {
				if (type == 2 && check(getLevel().PosToInt(getX(), getY() - 1, getZ())) == 1)
					add(getX(), getY() - 1, getZ());
				time++;
				return;
			}
			int x1 = random.nextInt(3);
			int x2 = random.nextInt(3);
			int z1 = random.nextInt(3);
			int z2 = random.nextInt(3);
			int i = 0;
			while (i != x1 && (getLevel().getTile(getX() + i + 1, getY(), getZ()).getVisableBlock() == 0 || getLevel().getTile(getX() + i + 1, getY(), getZ()).getVisableBlock() == this.getVisableBlock())) {
				i++;
				if (check(getLevel().PosToInt(getX() + i, getY(), getZ())) == 0 || check(getLevel().PosToInt(getX() + i, getY(), getZ())) == 2)
					break;
				else
					add(getX() + i, getY(), getZ());
			}
			i = 0;
			while (i != x2 && (getLevel().getTile(getX() - i - 1, getY(), getZ()).getVisableBlock() == 0 || getLevel().getTile(getX() - i - 1, getY(), getZ()).getVisableBlock() == this.getVisableBlock())) {
				i++;
				if (check(getLevel().PosToInt(getX() - i, getY(), getZ())) == 0 || check(getLevel().PosToInt(getX() - i, getY(), getZ())) == 2)
					break;
				else
					add(getX() - i, getY(), getZ());
			}
			i = 0;
			while (i != z1 && (getLevel().getTile(getX(), getY(), getZ() + i + 1).getVisableBlock() == 0 || getLevel().getTile(getX(), getY(), getZ() + i + 1).getVisableBlock() == this.getVisableBlock())) {
				i++;
				if (check(getLevel().PosToInt(getX(), getY(), getZ() + i)) == 0 || check(getLevel().PosToInt(getX(), getY(), getZ() + i)) == 2)
					break;
				else
					add(getX(), getY(), getZ() + i);
			}
			i = 0;
			while (i != z2 && (getLevel().getTile(getX(), getY(), getZ() - i - 1).getVisableBlock() == 0 || getLevel().getTile(getX(), getY(), getZ() - i - 1).getVisableBlock() == this.getVisableBlock())) {
				i++;
				if (check(getLevel().PosToInt(getX(), getY(), getZ() - i)) == 0 || check(getLevel().PosToInt(getX(), getY(), getZ() - i)) == 2)
					break;
				else
					add(getX(), getY(), getZ() - i);
			}
			if (check(getLevel().PosToInt(getX(), getY() - 1, getZ())) == 1)
				add(getX(), getY() - 1, getZ());
			//TODO Add other kinds of physics
		}
	}
	public int check(int x, int y, int z) {
		return check(getLevel().PosToInt(x, y, z));
	}
	public int check(int b) {
		System.out.println(wait);
		int temp = wait + 1;
		wait = 0;
		if (b < 0 || b >= getLevel().getLength()) {
			wait = temp;
			return 2;
		}
		Block bb = getLevel().getTile(b);
		byte btype = bb.getVisableBlock();
		if (bb.getVisableBlock() == 0 && !nearSponge(b))
			return 1;
		if ((bb.getVisableBlock() == 41 || bb.getVisableBlock() == 15) && type >= 5)
			return 1;
		if ((btype == 16 || btype == 47) && type >= 4)
			return 1;
		if (btype >= 21 && btype <= 36 && type >= 3)
			return 1;
		if ((btype == 5 || btype == 17 || btype == 18 || btype == 39 || btype == 40) && type > 1)
			return 1;
		if ((btype == 1 || btype == 4 || btype == 9 || btype == 20 || btype == 42 || btype == 49) && type == 6)
			return 1;
		if (btype == this.getVisableBlock()) {
			wait = temp;
			return 2;
		}
		return 0;
	}
	public boolean nearSponge(int b) {
		for (int x = -2; x <= +2; ++x) {
			for (int y = -2; y <= +2; ++y) {
				for (int z = -2; z <= +2; ++z) {
					if (getLevel().getTile(IntOffset(b, x, y, z)) == Block.getBlock((byte)19))
						return true;
				}
			}
		}
		return false;
	}
	public int IntOffset(int index, int x, int y, int z) {
		return index + x + z * getLevel().width + y * getLevel().width * getLevel().height;
	}

}

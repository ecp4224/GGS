/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.API.player;

import com.gamezgalaxy.GGS.API.Cancelable;
import com.gamezgalaxy.GGS.API.EventList;
import com.gamezgalaxy.GGS.iomodel.Player;
import com.gamezgalaxy.GGS.server.Server;
import com.gamezgalaxy.GGS.world.Block;
import com.gamezgalaxy.GGS.world.Level;

public class PlayerBlockPlaceEvent extends PlayerEvent implements Cancelable {

	private static EventList events = new EventList();
	
	private boolean _canceled; 
	
	private Block block;
	
	private Player who;
	
	private short X;
	private short Y;
	private short Z;
	private Level level;
	private Server server;
	
	
	public PlayerBlockPlaceEvent(Player who, short X, short Y, short Z, Block id, Level level, Server server) {
		super(who);
		this.who = who;
		this.block = id;
		this.X = X;
		this.Y = Y;
		this.Z = Z;
		this.level = level;
		this.server = server;
	}
	
	@Override
	public EventList getEvents() {
		return events;
	}
	
	public static EventList getEventList() {
		return events;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public void setBlock(Block block) {
		this.level.setTile(block, X, Y, Z, server);
		Player.GlobalBlockChange(X, Y, Z, block, level, server);
		this.block = block;
	}
	/*
	public void setBlock(short X, short Y, short Z, Block id, Level l, Server s) {
		Player.GlobalBlockChange(X, Y, Z, id, l, s);
		this.block = id;
	}
	*/
	@Override
	public boolean isCancelled() {
		return _canceled;
	}

	@Override
	public void Cancel(boolean cancel) {
		_canceled = cancel;
	}
}

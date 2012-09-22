/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.player;

import net.mcforge.API.Cancelable;
import net.mcforge.API.EventList;
import net.mcforge.iomodel.Player;

public class PlayerMoveEvent extends PlayerEvent implements Cancelable {
	private static EventList events = new EventList();
	
	private boolean _canceled;
	
	private short X;
	
	private short Y;
	
	private short Z;
	
	public PlayerMoveEvent(Player who) {
		this(who, (short)0, (short)0, (short)0);
	}
	
	public PlayerMoveEvent(Player who, short x, short y, short z) {
		super(who);
		this.X = x;
		this.Y = y;
		this.Z = z;
	}

	@Override
	public EventList getEvents() {
		return events;
	}
	/**
	 * Get a list of registered listeners
	 * @return The list of listeners
	 */
	public static EventList getEventList() {
		return events;
	}

	@Override
	public boolean isCancelled() {
		return _canceled;
	}

	@Override
	public void Cancel(boolean cancel) {
		_canceled = cancel;
	}
	
	public short getX() {
		return X;
	}
	
	public short getY() {
		return Y;
	}
	
	public short getZ() {
		return Z;
	}

}

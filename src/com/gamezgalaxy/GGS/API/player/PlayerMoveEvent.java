package com.gamezgalaxy.GGS.API.player;

import com.gamezgalaxy.GGS.API.Cancelable;
import com.gamezgalaxy.GGS.API.EventList;
import com.gamezgalaxy.GGS.server.Player;

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

}

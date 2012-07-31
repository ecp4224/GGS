package com.gamezgalaxy.GGS.API.player;

import com.gamezgalaxy.GGS.API.Cancelable;
import com.gamezgalaxy.GGS.API.EventList;
import com.gamezgalaxy.GGS.iomodel.Player;

public class PlayerConnectEvent extends PlayerEvent implements Cancelable {

	private static EventList events = new EventList();
	
	private boolean _canceled = false;
	
	private String kickmsg = "";
	
	public PlayerConnectEvent(Player who) {
		super(who);
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
	
	public void setKickMessage(String msg) {
		kickmsg = msg;
	}
	
	public String getKickMessage() {
		return kickmsg;
	}

}

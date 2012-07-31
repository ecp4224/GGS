package com.gamezgalaxy.GGS.API.player;

import com.gamezgalaxy.GGS.API.EventList;
import com.gamezgalaxy.GGS.iomodel.Player;

public class PlayerDisconnectEvent extends PlayerEvent {
	private static EventList events = new EventList();
	
	public PlayerDisconnectEvent(Player who) {
		super(who);
	}

	@Override
	public EventList getEvents() {
		return events;
	}
	public static EventList getEventList() {
		return events;
	}
}

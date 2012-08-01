package com.gamezgalaxy.GGS.API.browser;

import com.gamezgalaxy.GGS.API.EventList;
import com.gamezgalaxy.GGS.iomodel.Browser;

public class BrowserConnectEvent extends BrowserEvent {

	private static EventList events = new EventList();
	
	public BrowserConnectEvent(Browser b) {
		super(b);
	}

	@Override
	public EventList getEvents() {
		return events;
	}
	public static EventList getEventList() {
		return events;
	}

}

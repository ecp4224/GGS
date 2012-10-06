package com.gamezgalaxy.GGS.API.level;

import com.gamezgalaxy.GGS.API.EventList;
import com.gamezgalaxy.GGS.API.server.ServerEvent;
import com.gamezgalaxy.GGS.server.Server;
import com.gamezgalaxy.GGS.world.LevelHandler;

public class AllLevelLoadedEvent extends ServerEvent {
	
	private LevelHandler lh;
	
	private static EventList events = new EventList();
	
	public AllLevelLoadedEvent(Server s, LevelHandler lh) {
		super(s);
		this.lh = lh;
	}
	
	/**
	 * Get the {@link LevelHandler} that loaded all the levels
	 * @return
	 *        The {@link LevelHandler}
	 */
	public LevelHandler getLevelHandler() {
		return lh;
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

}

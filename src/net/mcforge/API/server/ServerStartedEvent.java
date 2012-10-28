package net.mcforge.API.server;

import net.mcforge.API.EventList;
import net.mcforge.server.Server;

public class ServerStartedEvent extends ServerEvent {

	private static EventList events = new EventList();
	public ServerStartedEvent(Server server) {
		super(server);
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


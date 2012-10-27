package net.mcforge.API.server;

import net.mcforge.API.Event;
import net.mcforge.API.EventList;
import net.mcforge.server.Server;

public abstract class ServerEvent extends Event  {
	
	private Server server;
	
	public ServerEvent(Server server) {
		this.server = server;
	}
	
	/**
	 * Get the server this event took place in
	 * @return
	 *        The {@link Server} object
	 */
	public Server getServer() {
		return server;
	}
	
	/**
	 * Get a list of registered listeners
	 * @return The list of listeners
	 */
	public abstract EventList getEvents();

}

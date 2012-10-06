package com.gamezgalaxy.GGS.API.server;

import com.gamezgalaxy.GGS.API.Event;
import com.gamezgalaxy.GGS.server.Server;

public abstract class ServerEvent extends Event {
	
	private Server server;
	
	public ServerEvent(Server server) {
		this.server = server;
	}
	
	/**
	 * Get the server that launched this event.
	 * @return
	 *        The {@link Server} object
	 */
	public Server getServer() {
		return server;
	}

}

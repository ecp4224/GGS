package com.gamezgalaxy.GGS.API.io;

import com.gamezgalaxy.GGS.API.Event;
import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.server.Server;

public abstract class IOEvent extends Event {
	private IOClient _client;
	private Server _server;
	public IOEvent(IOClient client, Server server) {
		this._client = client;
		this._server = server;
	}
	
	/**
	 * Get the IOClient associated with this event
	 * @return The IOClient
	 */
	public IOClient getIOClient() {
		return _client;
	}
	
	/**
	 * Get the server this event took place in
	 * @return The server
	 */
	public Server getServer() {
		return _server;
	}
}

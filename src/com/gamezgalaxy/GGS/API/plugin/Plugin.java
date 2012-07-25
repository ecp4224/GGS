package com.gamezgalaxy.GGS.API.plugin;

import com.gamezgalaxy.GGS.server.Server;

public abstract class Plugin {
	
	private Server _server;
	
	public Plugin(Server server) {
		this._server = server;
	}
	
	public abstract void onLoad(String[] args);
	
	public abstract void onUnload();
	
	public Server getServer() {
		return _server;
	}

}

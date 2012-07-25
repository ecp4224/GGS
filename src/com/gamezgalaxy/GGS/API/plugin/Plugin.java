/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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

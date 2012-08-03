/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.API.plugin;

import com.gamezgalaxy.GGS.server.Server;

import java.util.Properties;

public abstract class Plugin
{
	private Server _server;
	private Properties properties;
	
	public Plugin(Server server, Properties properties)
	{
		this._server = server;
		this.properties = properties;
	}
	public Plugin(Server server) {
		this._server = server;
	}
	
	public abstract void onLoad(String[] args);
	
	public abstract void onUnload();

	public String getName_()
	{
		return properties.getProperty("name");
	}

	public String getVersion()
	{
		return properties.getProperty("version");
	}

	public String getAuthor()
	{
		return properties.getProperty("author");
	}

	public Server getServer()
	{
		return _server;
	}

	public Properties getProperties()
	{
		return properties;
	}
}

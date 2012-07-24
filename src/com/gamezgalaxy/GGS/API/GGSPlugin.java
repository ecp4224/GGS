package com.gamezgalaxy.GGS.API;

import com.gamezgalaxy.GGS.server.Server;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/23/12
 * Time: 9:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class GGSPlugin
{
	// TODO: Create a config system and stuff.

	public GGSPlugin()
	{
	}

	public enum STATE
	{
		INIT,
		ENABLE,
		DISABLE,
	}

	private STATE state;
	private Server server;

	public void onEnable()
	{
		state = STATE.ENABLE;
	}

	public void onDisable()
	{
		state = STATE.DISABLE;
	}

	public void initialize(Server server)
	{
		// TODO: Initialize other stuff like config files.

		this.server = server;
	}
}

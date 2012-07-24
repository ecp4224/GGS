package com.gamezgalaxy.GGS.API;

import com.gamezgalaxy.GGS.server.Player;
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

	private Server server;
	private boolean enabled;
	private String lastCommand;
	private String[] lastArgs;

	public void onEnable()
	{
		enabled = true;
	}

	public void onDisable()
	{
		enabled = false;
	}

	public void onCommand(Player sender, String command, String[] args)
	{
		lastCommand = command;
		lastArgs = args;
	}

	public void initialize(Server server)
	{
		// TODO: Initialize other stuff like config files.

		this.server = server;
	}
}

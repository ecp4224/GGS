package com.gamezgalaxy.GGS.defaults.commands;

import com.gamezgalaxy.GGS.API.plugin.Command;
import com.gamezgalaxy.GGS.iomodel.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/27/12
 * Time: 9:18 PM
 */
public class Afk extends Command
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "afk";
	}

	@Override
	public boolean isOpCommand()
	{
		return false;
	}

	@Override
	public int getDefaultPermissionLevel()
	{
		return 0;
	}

	@Override
	public void execute(Player player, String[] args)
	{
		if(player.isAfk())
		{
			player.setAfk(false);

			player.getChat().serverBroadcast(player.username + " is no longer afk.");
		} else {
			player.setAfk(true);

			player.getChat().serverBroadcast(player.username + " is now afk...");
		}
	}
}

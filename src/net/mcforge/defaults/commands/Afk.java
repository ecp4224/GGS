/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.defaults.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;

public class Afk extends PlayerCommand
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

	@Override
	public void help(CommandExecutor executor) {
		// TODO Auto-generated method stub
		
	}
}

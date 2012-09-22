/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.defaults.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;
import net.mcforge.world.LevelHandler;


public class Newlvl extends Command
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "newlvl";
	}

	@Override
	public boolean isOpCommand()
	{
		return true;
	}

	@Override
	public int getDefaultPermissionLevel()
	{
		return 0;
	}

	@Override
	public void execute(CommandExecutor player, String[] args)
	{
		if(args.length == 4)
		{
			LevelHandler handler = player.getServer().getLevelHandler();
			handler.loadLevels();
			if(handler.findLevel(args[0]) == null)
			{
				handler.newLevel(args[0], Short.valueOf(args[1]), Short.valueOf(args[2]), Short.valueOf(args[3]));
				player.sendMessage("Created new level: " + args[0] + ".");
			} else {
				player.sendMessage("Level already exists...");
			}
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		// TODO Auto-generated method stub
		
	}
}

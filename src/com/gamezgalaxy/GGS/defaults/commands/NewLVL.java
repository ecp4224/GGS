/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.defaults.commands;

import com.gamezgalaxy.GGS.API.plugin.Command;
import com.gamezgalaxy.GGS.iomodel.Player;
import com.gamezgalaxy.GGS.world.Level;
import com.gamezgalaxy.GGS.world.LevelHandler;

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
	public void execute(Player player, String[] args)
	{
		if(args.length == 4)
		{
			LevelHandler handler = player.getServer().getLevelHandler();
			handler.loadLevels();
			Level[] levels = handler.levels.toArray(new Level[handler.levels.size()]);

			if(handler.findLevel(args[1]) == null)
			{
				handler.newLevel(args[1], Short.valueOf(args[2]), Short.valueOf(args[3]), Short.valueOf(args[4]));

				player.sendMessage("Created new level: " + args[1] + ".");
			} else {
				player.sendMessage("Level already exists...");
			}
		} else {
			player.sendMessage("Correct format: /newlvl (name) (width) (height) (length)");
		}
	}
}

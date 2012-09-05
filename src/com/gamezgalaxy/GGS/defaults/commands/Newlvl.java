package com.gamezgalaxy.GGS.defaults.commands;

import com.gamezgalaxy.GGS.API.CommandExecutor;
import com.gamezgalaxy.GGS.API.plugin.Command;
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
}

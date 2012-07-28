package com.gamezgalaxy.GGS.defaults.commands;

import com.gamezgalaxy.GGS.API.plugin.Command;
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.world.Level;
import com.gamezgalaxy.GGS.world.LevelHandler;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/25/12
 * Time: 3:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class Load extends Command
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "load";
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
		if(args.length == 2)
		{
			LevelHandler handler = player.getServer().getLevelHandler();
			File levelFile = new File("levels/" + args[1] + ".ggs");

			if(levelFile.exists())
			{
				handler.loadLevel(levelFile.getPath());
			} else {
				player.sendMessage("Level does not exist.");
			}
		}
	}
}

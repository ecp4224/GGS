package com.gamezgalaxy.GGS.defaults.commands;

import com.gamezgalaxy.GGS.API.plugin.Command;
import com.gamezgalaxy.GGS.iomodel.Player;
import com.gamezgalaxy.GGS.world.Level;
import com.gamezgalaxy.GGS.world.LevelHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/25/12
 * Time: 3:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class Loaded extends Command
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "loaded";
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
		LevelHandler handler = player.getServer().getLevelHandler();
		StringBuilder finalStr = new StringBuilder();

		for(Level l : handler.levels)
		{
			finalStr.append(l.name);
			finalStr.append(", ");
		}

		player.sendMessage(finalStr.toString());
	}
}

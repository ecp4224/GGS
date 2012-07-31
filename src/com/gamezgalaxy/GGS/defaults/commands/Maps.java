package com.gamezgalaxy.GGS.defaults.commands;

import com.gamezgalaxy.GGS.API.plugin.Command;
import com.gamezgalaxy.GGS.iomodel.Player;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/25/12
 * Time: 3:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class Maps extends Command
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "maps";
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
		File levelsFolder = new File("levels");
		File[] levelFiles = levelsFolder.listFiles();
		StringBuilder finalStr = new StringBuilder();

		for(File f : levelFiles)
		{
			if(f.getName().split("\\.")[1].equals("ggs"))
			{
				finalStr.append(f.getName().split("\\.")[0]);
				finalStr.append(", ");
			}
		}

		player.sendMessage(finalStr.toString());
	}
}

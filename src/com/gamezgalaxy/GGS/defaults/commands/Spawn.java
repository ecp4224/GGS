package com.gamezgalaxy.GGS.defaults.commands;

import com.gamezgalaxy.GGS.API.plugin.Command;
import com.gamezgalaxy.GGS.server.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/25/12
 * Time: 3:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class Spawn extends Command
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "spawn";
	}

	@Override
	public boolean isOpCommand()
	{
		return false;
	}

	@Override
	public int getDefaultPermissionLevel()
	{
		return 0; // DON'T KNOW THE PERMISSION LEVEL FOR STANDARD USER.
	}

	@Override
	public void execute(Player player, String[] args)
	{
		player.setPos((short)((0.5 + player.getLevel().spawnx) * 32), (short)((1 + player.getLevel().spawny) * 32), (short)((0.5 + player.getLevel().spawnz) * 32));
	}
}

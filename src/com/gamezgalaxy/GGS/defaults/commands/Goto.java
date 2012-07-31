package com.gamezgalaxy.GGS.defaults.commands;

import com.gamezgalaxy.GGS.API.plugin.Command;
import com.gamezgalaxy.GGS.iomodel.Player;
import com.gamezgalaxy.GGS.world.Level;
import com.gamezgalaxy.GGS.world.LevelHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/25/12
 * Time: 3:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class Goto extends Command
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "goto";
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
		if(args.length == 2)
		{
			LevelHandler handler = player.getServer().getLevelHandler();
			Level level = handler.findLevel(args[1]);

			if(level != null)
			{
				try {
					player.changeLevel(level);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				player.sendMessage("Level doesn't exist...");
			}
		} else {
			player.sendMessage("Correct format: /goto (level name)");
		}
	}
}

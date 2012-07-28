package com.gamezgalaxy.GGS.defaults.commands;

import com.gamezgalaxy.GGS.API.plugin.Command;
import com.gamezgalaxy.GGS.server.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/27/12
 * Time: 9:18 PM
 */
public class TP extends Command
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "tp";
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
			Player other = player.getServer().getPlayer(args[1]);
			if(other != null)
			{
				player.setPos(other.getX(), other.getY(), other.getZ());
			} else {
				player.sendMessage(args[1] + " is not online.");
			}
		} else if(args.length == 3) {
			Player otherTPing = player.getServer().getPlayer(args[1]);
			Player otherTPto = player.getServer().getPlayer(args[2]);

			if(otherTPing != null && otherTPto != null)
			{
				otherTPing.setPos(otherTPto.getX(), otherTPto.getY(), otherTPto.getZ());
			} else {
				player.sendMessage("Either " + args[1] + " or " + args[2] + " is not online.");
			}
		} else {
			player.sendMessage("Correct format: /tp (player) or /tp (player) (to player)");
		}
	}
}

package net.mcforge.defaults.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.groups.Group;
import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;

public class Me extends PlayerCommand
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "me";
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
		if (args[0] == "")
		{
			player.sendMessage("/me <message> - Sends <message> under " + player.username);
		}
		else
		{
			String message = "";
			for (String s : args) {
				message += " " + s;
			}
			message = message.trim();
			for (Player p : player.getServer().players)
				p.sendMessage("&6~" + player.getDisplayColor() + message);
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		// TODO Auto-generated method stub

	}
}
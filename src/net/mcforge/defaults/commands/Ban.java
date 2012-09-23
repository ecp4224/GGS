package net.mcforge.defaults.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;
import net.mcforge.system.BanHandler;

public class Ban extends PlayerCommand {

	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "ban";
	}

	@Override
	public boolean isOpCommand() {
		return true;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 100;
	}

	@Override
	public void execute(Player player, String[] args) {
		if (args.length != 0) {
			if (BanHandler.isBanned(args[0])) {
				player.sendMessage(args[0] + " is already banned");
				return;
			}
			if (!args[0].equalsIgnoreCase(player.username)) {
				Player who = player.getServer().getPlayer(args[0]);
				if (who != null) {
					BanHandler.ban(who.username);
					player.getServer().getMessages().serverBroadcast(who.username + " has been banned by " + player.username);
				}
				else {
					BanHandler.ban(args[0]);
					player.getServer().getMessages().serverBroadcast(args[0] + " has been banned by " + player.username);
				}
			}
			else {
				player.sendMessage("You cannot ban yourself.");
			}
		}
		else {
			help(player);
		}
		// TODO Add expire date
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/ban <player> - bans the specified player!");
	}

}

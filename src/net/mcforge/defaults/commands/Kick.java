package net.mcforge.defaults.commands;

import java.util.Arrays;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;
import net.mcforge.chat.Messages;
import net.mcforge.iomodel.Player;

public class Kick extends Command {
	@Override
	public String[] getShortcuts() {
		return new String[] { "k" };
	}

	@Override
	public String getName() {
		return "kick";
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
	public void execute(CommandExecutor player, String[] args) {
		if (args.length == 0) {
			help(player);
			return;
		}
		String kickReason = "";
		Player who = player.getServer().getPlayer(args[0]);
		if (who == null) {
			player.sendMessage("Player not found!");
			return;
		}
		if (who.equals(player)) {
			player.sendMessage("You can't kick yourself!");
			return;
		}
		//TODO: if (perm > p.perm) { no kick for you }
		if (args.length > 1) { 
			kickReason = Messages.join(Arrays.copyOfRange(args, 1, args.length));
		}
		else {
			kickReason = "You have been kicked!";
		}
		who.kick(kickReason);
	}

	@Override
	public void help(CommandExecutor player) {
		player.sendMessage("/kick <player> [reason] - kicks the specified player with the specified reason");
	}
}
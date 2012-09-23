package net.mcforge.defaults.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;

public class Help extends Command {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public boolean isOpCommand() {
		return false;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(CommandExecutor player, String[] args) {
		if (args.length == 0) {
			help(player);
			return;
		}
		Command cmd = player.getServer().getCommandHandler().find(args[0]);
		if (cmd == null) {
			player.sendMessage("The specified command wasn't found!");
			return;
		}
		cmd.help(player);
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/help <commandname> - shows help for the specified command");
	}
}
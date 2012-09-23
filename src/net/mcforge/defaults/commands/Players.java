package net.mcforge.defaults.commands;

import java.util.List;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;
import net.mcforge.iomodel.Player;

public class Players extends Command {
	@Override
	public String[] getShortcuts() {
		return new String[] { "online", "who" };
	}

	@Override
	public String getName() {
		return "players";
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
		List<Player> players = player.getServer().players;
		int size = players.size();
		if (size == 0) {
			player.sendMessage("There are no online players!");
			return;
		}
		String list = "";
		for (int i = 0; i < size; i++) {
			list += players.get(i).username + ", ";
		}
		list = list.substring(0, list.length() - 2);
		player.sendMessage(String.format("There %s %d player%s online: %s",
				size == 1 ? "is" : "are", size, size == 1 ? "" : "s", list));
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/players - shows the list of online players");
		executor.sendMessage("Shortcuts: /online, /who");
	}
}
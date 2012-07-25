package com.gamezgalaxy.GGS.API.plugin;

import java.util.ArrayList;

import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class CommandHandler {
	private static ArrayList<Command> commands = new ArrayList<Command>();
	
	private Server _server;
	
	public CommandHandler(Server server) {
		this._server = server;
	}
	
	public void execute(Player player, String name, String message) {
		execute(player, name, message.split(" "));
	}
	
	public Command find(String name) {
		for (Command c : commands) {
			if (name.equalsIgnoreCase(c.getName()))
				return c;
			else {
				for (String shortcut : c.getShortcuts()) {
					if (name.equalsIgnoreCase(shortcut))
						return c;
				}
			}
		}
		return null;
	}

	public void execute(Player player, String command, String[] args) {
		if (find(command) == null) {
			if (player != null)
				player.sendMessage("Command not found!");
			else
				_server.Log("Command not found!");
		}
		Command c = find(command);
		if (player != null && !player.getGroup().canExecute(c))
			player.sendMessage("Sorry, you dont have permission to execute this command!");
		else
			c.execute(player, args);
	}
	
	public void loadPermissions() {
		//TODO Load all command permissions
	}
	
	public void savePermissions() {
		//TODO Save all command permissions
	}

}

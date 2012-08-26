/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.API.plugin;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.gamezgalaxy.GGS.iomodel.Player;
import com.gamezgalaxy.GGS.server.Server;

public class CommandHandler {
	public ArrayList<Command> commands = new ArrayList<Command>();
	
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
		if (args.length == 1 && args[0].equals(""))
			args = new String[0];
		if(c != null)
		{
			if (player != null && !player.getGroup().canExecute(c))
				player.sendMessage("Sorry, you dont have permission to execute this command!");
			else {
				_server.Log(player.username + " used /" + c.getName() + arrayToString(args));
				c.execute(player, args);
			}
		}
	}
	
	public String arrayToString(String[] args) {
		String finals = "";
		for (String s : args) {
			finals += " " + s;
		}
		return finals;
	}
	public void addCommand(Command cmd) {
		if (commands.contains(cmd))
			return;
		commands.add(cmd);
	}
	
	public void removeCommand(Command cmd) {
		if (!commands.contains(cmd))
			return;
		commands.remove(cmd);
	}
	
	public void removeCommand(String name) {
		Command c = find(name);
		if (c == null)
			return;
		removeCommand(c);
	}
	
	public void loadPermissions() throws IOException {
		if (!new File("properties").exists())
			new File("properties").mkdir();
		if (!new File("properties/commands.config").exists())
			makeDefault();
		FileInputStream fstream = new FileInputStream("properties/commands.config");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			if (strLine.startsWith("#"))
				continue;
			String cmdname = strLine.split("\\:")[0];
			for (Command c : commands) {
				if (c.getName().equalsIgnoreCase(cmdname)) {
					c.setPermissionLevel(Integer.parseInt(strLine.split("\\:")[1]));
					break;
				}
			}
		}
		in.close();
	}
	
	public void makeDefault() {
		PrintWriter out = null;
		try {
			new File("properties/commands.config").createNewFile();
			out = new PrintWriter("properties/commands.config");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		out.println("#Permission file for commands");
		out.println("#Name:Permission Level");
		for (Command c : commands) {
			out.println(c.getName() + ":" + c.getDefaultPermissionLevel());
		}
		out.flush();
		out.close();
	}
	
	public void savePermissions() throws IOException {
		if (new File("properties/commands.config").exists())
			new File("properties/commands.config").delete();
		new File("properties/commands.config").createNewFile();
		PrintWriter out = null;
		try {
			out = new PrintWriter("properties/commands.config");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		out.println("#Permission file for commands");
		out.println("#Name:Permission Level");
		for (Command c : commands) {
			out.println(c.getName() + ":" + c.getPermissionLevel());
		}
		out.flush();
		out.close();
	}

}

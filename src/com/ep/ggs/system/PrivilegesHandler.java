/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.system;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ep.ggs.API.CommandExecutor;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.server.Server;
import com.ep.ggs.util.FileUtils;
import com.ep.ggs.util.WebUtils;
import com.ep.ggs.util.properties.Properties;


/**
 * This class handles the MCForge Staff Privileges system.
 * All of the properties for the system along with the support 
 * for it is located in this class.
 */
public class PrivilegesHandler {
	
	public enum StaffRank { 
		Developer(3), Moderator(2), GCStaff(1), None(0);
		int value;
		StaffRank(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		public static StaffRank fromValue(int value) {
			StaffRank[] values = values();
			for (int i = 0; i < values.length; i++) {
				if (values[i].value == value) {
					return values[i];
				}
			}
			return null;
		}
	}
	
	private Server server;
	private Properties properties = new Properties();
	
	private List<String> developers = new ArrayList<String>();
	private List<String> moderators = new ArrayList<String>();
	private List<String> gcstaff = new ArrayList<String>();
	
	private final String allowedCommandsPath = "allowedcommands.config";
	private final String protectedCommandsPath = "protectedcommands.config";
	private final String propertiesPath = "staffprivileges.config";
	
	private HashMap<Command, StaffRank> allowedCommands = new HashMap<Command, StaffRank>();
	private HashMap<Command, StaffRank> protectedCommands = new HashMap<Command, StaffRank>();
	
	/**
	 * Whether the privileges system is enabled.
	 * Setting this to false will override both the 
	 * {@link #privilegesEnabled} property and the 
	 * {@link #protectionEnabled} property.
	 */
	public boolean privilegesEnabled = true;
	/**
	 * Whether the perks system is enabled. 
	 */
	public boolean perksEnabled = true;
	/**
	 * Whether the protection system is enabled.
	 */
	public boolean protectionEnabled = true;
	
	public PrivilegesHandler(Server server) {
		this.server = server;
	}
	
	/**
	 * Initializes the PrivilegesHandler. Use this method
	 * to fully load the PrivilegesHandler.
	 */
	public synchronized void initialize() throws IOException {
		if (!loadStaffRanks()) {
			return;
		}
		loadProperties();
		loadAllowedCommands();
		loadProtectedCommands();
	}
	
	/**
	 * Loads the list of MCForge staff. The list is
	 * retrieved from the URL <i>http://server.mcforge.net/devs.txt</i>.
	 */
	public synchronized boolean loadStaffRanks() {
		developers.clear();
		moderators.clear();
		gcstaff.clear();
		String[] staffList;
		try {
			staffList = WebUtils.readContentsToArray(new URL("http://server.mcforge.net/devs.txt"));
		}
		catch (MalformedURLException e) {
			server.Log("Couldn't retrieve the MCForge staff list!");
			server.logError(e);
			return false;
		}
		catch (IOException e) {
			server.Log("Couldn't retrieve the MCForge staff list!");
			server.logError(e);
			return false;
		}
		for (int i = 0; i < staffList.length; i++) {
			String[] split = staffList[i].split(":");
			String[] staff = split[1].split(" ");
			
			if (split[0].equals("devs"))
				for (int j = 0; j < staff.length; j++)
					developers.add(staff[j]);
			else if (split[0].equals("mods"))
				for (int j = 0; j < staff.length; j++)
					moderators.add(staff[j]);
			else if (split[0].equals("gcmods"))
				for (int j = 0; j < staff.length; j++)
					gcstaff.add(staff[j]);
		}
		return true;
	}

	/**
	 * Loads the commands that MCForge staff is allowed to use
	 * despite their permission level.
	 */
	public synchronized void loadAllowedCommands() throws IOException {
		allowedCommands.clear();
		FileUtils.createIfNotExist(FileUtils.PROPS_DIR, allowedCommandsPath, 
				"#This is the config file for the MCForge Staff Privileges System\r\n" + 
				"#Here you can add the commands that you want to allow MCForge staff to use despite their permission level\r\n" +
			    "#MCForge Staff can use the commands to moderate the server or get information, if needed\r\n" +
				"#To add a command follow this format: command name:permissionlevel\r\n" +
			    "#The permission levels are: 3 - Developer, 2 - Moderator, 1 - Global Chat Moderator\r\n" +
			    "#The permission level is the minimal MCForge staff rank that can use the command. If it's invalid " +
			    "the command will be ignored\r\n" + 
			    "#Example:\tban:3 Will give MCForge devs the right to use the /ban command\r\n" +
			    "#        \tkick:2 Will give MCForge mods and devs the right to use the /kick command\r\n" + 
			    "#If the command doesn't exist on your server, it will be ignored"
				);
		String[] lines = FileUtils.readAllLines(FileUtils.PROPS_DIR + allowedCommandsPath);
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.startsWith("#")) {
				continue;
			}
			
			String[] split = line.split(":");
			if (split.length != 2) {
				continue;
			}
			
			int commandperm;		
			try {
				commandperm = Integer.parseInt(split[1]);
			}
			catch(NumberFormatException ex) {
				continue;
			}
			if (commandperm < 1 || commandperm > 3) {
				continue;
			}
			
			Command c = server.getCommandHandler().find(split[0]);
			if (c == null) {
				continue;
			}
			
			allowedCommands.put(c, StaffRank.fromValue(commandperm));
		}
				
	}

	/**
	 * Loads the commands that MCForge staff is protected from.
	 */
	public synchronized void loadProtectedCommands() throws IOException {
		protectedCommands.clear();
		FileUtils.createIfNotExist(FileUtils.PROPS_DIR, protectedCommandsPath, 
				"#This is the config file for the MCForge Staff Privileges System\r\n" + 
				"#Here you can add the commands whose effects you want MCForge staff to have protection from\r\n" +
			    "#The commands listed here won't be usable on MCForge staff\r\n" +
				"#To add a command follow this format: command name:permissionlevel\r\n" +
			    "#The permission levels are: 3 - Developer, 2 - Moderator, 1 - Global Chat Moderator\r\n" +
			    "#The permission level is the minimal MCForge staff rank that is protected from the command. If it's invalid " +
			    "the command will be ignored\r\n" + 
			    "#Example:\tban:3 Won't allow MCForge devs to be banned\r\n" +
			    "#        \tkick:2 Won't allow MCForge mods and devs to be kicked\r\n" + 
			    "#If the command doesn't exist on your server, it will be ignored"
				);
		String[] lines = FileUtils.readAllLines(FileUtils.PROPS_DIR + protectedCommandsPath);
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.startsWith("#")) {
				continue;
			}
			
			String[] split = line.split(":");
			if (split.length != 2) {
				continue;
			}
			
			int commandperm;		
			try {
				commandperm = Integer.parseInt(split[1]);
			}
			catch(NumberFormatException ex) {
				continue;
			}
			if (commandperm < 1 || commandperm > 3) {
				continue;
			}
			
			Command c = server.getCommandHandler().find(split[0]);
			if (c == null) {
				continue;
			}
			
			protectedCommands.put(c, StaffRank.fromValue(commandperm));
		}
	}

	/**
	 * Loads the properties for the PrivilegesHandler.
	 * Note that this only loads the <i>staffprivileges.config</i>
	 * file. To load allowed and protected commands use
	 * {@link #loadProtectedCommands()} or {@link #loadAllowedCommands()}.
	 */
	public synchronized void loadProperties() throws IOException {
		addDefaults();
		privilegesEnabled = properties.getBool("Privileges-Enabled");
		protectionEnabled = properties.getBool("Protection-Enabled");
		perksEnabled = properties.getBool("Perks-Enabled");
	}
	
	private void addDefaults() throws IOException {
		FileUtils.createIfNotExist(FileUtils.PROPS_DIR, propertiesPath);
		
		properties.load(FileUtils.PROPS_DIR + propertiesPath);
		if (!properties.hasValue("Privileges-Enabled")) {
			properties.addSetting("Privileges-Enabled", true);
			properties.addComment("Privileges-Enabled", "Whether staff privileges are enabled. Setting this to false overrides both Protection-Enabled and Perks-Enabled.");
		}
		if (!properties.hasValue("Protection-Enabled")) {
			properties.addSetting("Protection-Enabled", true);
			properties.addComment("Protection-Enabled", "Whether the staff protection system is enabled.");
		}
		if (!properties.hasValue("Perks-Enabled")) {
			properties.addSetting("Perks-Enabled", true);
			properties.addComment("Perks-Enabled", "Whether the staff perks system is enabled.");
		}
		properties.save(FileUtils.PROPS_DIR + propertiesPath);
	}
	
	/**
	 * Checks if the specified player can override the
	 * command permissions for the specified command.
	 *
	 * @param p - The player to check for.
	 * @param command - The command to check for.
	 */
	public boolean canOverride(CommandExecutor p, Command command) {
		if (!(p instanceof Player)) { 
			return false; 
		}
		
		if (!perksEnabled || !allowedCommands.containsKey(command)) {
			return false;
		}
		return getStaffRank((Player)p).getValue() >= allowedCommands.get(command).getValue();
		
	}
	
	/**
	 * Checks if the specified player is protected
	 * from the specified command.
	 *
	 * @param p - The player to check for.
	 * @param command - The command to check for.
	 */
	public boolean isProtected(CommandExecutor p, Command command) {
		if (!(p instanceof Player)) { 
			return false; 
		}
		
		if (!protectionEnabled || !protectedCommands.containsKey(command)) {
			return false;
		}
		return getStaffRank((Player)p).getValue() >= protectedCommands.get(command).getValue();
	}
	
	/**
	 * Checks if the specified player is protected
	 * from the specified command.
	 *
	 * @param name - The name of the player to check for.
	 * @param command - The command to check for.
	 */
	public boolean isProtected(String name, Command command) {
		if (!protectionEnabled || !protectedCommands.containsKey(command)) {
			return false;
		}
		return getStaffRank(name).getValue() >= protectedCommands.get(command).getValue();
	}
	
	/**
	 * Gets a list of all MCForge staff. Developers, moderators and Global Chat staff.
	 */
	public List<String> getStaff() {
		List<String> staff = developers;
		staff.addAll(moderators);
		staff.addAll(gcstaff);
		return staff;
	}
	
	/**
	 * Gets the list of MCForge developers.
	 */
	public List<String> getDevs() {
		return developers;
	}
	
	/**
	 * Gets the list of MCForge moderators.
	 */
	public List<String> getMods() {
		return moderators;
	}
	
	/**
	 * Gets the list of MCForge Global Chat staff.
	 */
	public List<String> getGCStaff() {
		return gcstaff;
	}

	/**
	 * Checks if the specified name belongs to a member of the MCForge staff team.
	 * 
	 * @param name - The name of the player to check for.
	 */
	public boolean isStaff(String name) {
		return (developers.contains(name) || moderators.contains(name) || gcstaff.contains(name));
	}
	
	/**
	 * Checks if the specified player is the member of the MCForge staff team.
	 * 
	 * @param p - The player to check for.
	 */
	public boolean isStaff(Player p) {
		return isStaff(p.getName());
	}
	
	/**
	 * Checks if the specified name belongs to a member of the MCForge development team.
	 * 
	 * @param name - The name of the player to check for.
	 */
	public boolean isDev(String name) {
		return developers.contains(name);
	}
	
	/**
	 * Checks if the specified player is the member of the MCForge development team.
	 * 
	 * @param p - The player to check for.
	 */
	public boolean isDev(Player p) {
		return isDev(p.getName());
	}

	/**
	 * Checks if the specified name belongs to a member of the MCForge moderation team.
	 * 
	 * @param name - The name of the player to check for.
	 */
	public boolean isMod(String name) {
		return moderators.contains(name);
	}
	
	/**
	 * Checks if the specified player is the member of the MCForge moderation team.
	 *
	 * @param p - The player to check for.
	 */
	public boolean isMod(Player p) {
		return isMod(p.getName());
	}

	/**
	 * Checks if the specified name belongs to a member of the MCForge Global Chat moderation team.
	 * 
	 * @param name - The name of the player to check for.
	 */
	public boolean isGcStaff(String name) {
		return gcstaff.contains(name);
	}
	
	/**
	 * Checks if the specified player is the member of the MCForge Global Chat moderation team.
	 * 
	 * @param p - The player to check for.
	 */
	public boolean isGcStaff(Player p) {
		return isGcStaff(p.getName());
	}

	/**
	 * Gets the specified player's {@link StaffRank}
	 * 
	 * @param p - The player to get the rank for.
	 */
	public StaffRank getStaffRank(Player p) {
		if (isDev(p))
			return StaffRank.Developer;
		else if (isMod(p))
			return StaffRank.Moderator;
		else if (isGcStaff(p))
			return StaffRank.GCStaff;
		else
			return StaffRank.None;
	}
	
	/**
	 * Gets the specified player's {@link StaffRank}.
	 * 
	 * @param name - The name of the player to check for.
	 */
	public StaffRank getStaffRank(String name) {
		if (isDev(name))
			return StaffRank.Developer;
		else if (isMod(name))
			return StaffRank.Moderator;
		else if (isGcStaff(name))
			return StaffRank.GCStaff;
		else
			return StaffRank.None;
	}

	/**
	 * Gets the {@link Properties} for the PrivilegesHandler.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Checks if any rank of MCForge staff is protected from the specified {@link Command}.
	 * 
	 * @param c - The command to check for.
	 */
	public boolean isProtectedCmd(Command c) {
		return protectedCommands.containsKey(c);
	}
	
	/**
	 * Checks if any rank of the MCForge staff is allowed to use the specified {@link Command}.
	 */
	public boolean isAllowedCmd(Command c) {
		return allowedCommands.containsKey(c);
	}

	/**
	 * Gets the minimal {@link StaffRank} that is protected from the specified {@link Command}.
	 * 
	 * @param c - The command to check for.
	 */
	public StaffRank getProtectedRank(Command c) {
		StaffRank s = protectedCommands.get(c);
		return s == null ? StaffRank.None : s;
	}
	
	/**
	 * Gets the minimal {@link StaffRank} that is allowed to use the specified {@link Command}
	 * despite their permission level on the server.
	 * 
	 * @param c - The command to check for.
	 */
	public StaffRank getAllowedRank(Command c) {
		StaffRank s = allowedCommands.get(c);
		return s == null ? StaffRank.None : s;
	}
}

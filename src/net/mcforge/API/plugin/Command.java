/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.API.plugin;

import net.mcforge.API.CommandExecutor;

/**
* A command that can be executed by any {@link CommandExecutor} by typing
* \/commandname arg1 arg2 arg3 in-game, or any other method provided
* by plugins/extenders.
* 
* If you want a command that can only be used by a {@link Player}
* then use the {@link PlayerCommand} class
*/
public abstract class Command {
	
	private int permission;
	
	// -1 - not set
	// 0 - false
	// 1 - true
	private int opcommand;
	
	/**
	 * An array of shortcuts for this command
	 * @return
	 *        A list of commands that can be typed that will
	 *        also execute this command
	 */
	public abstract String[] getShortcuts();
	
	/**
	 * The name of this command
	 * @return
	 *        Name
	 */
	public abstract String getName();
	
	/**
	 * Weather this command is for ops only by default
	 * @return
	 *        True if only ops can use it, otherwise it will be false.
	 */
	public abstract boolean isOpCommandDefault();
	
	/**
	 * The default permission level for this command. This value
	 * is usually 0.
	 * If you want to get the true permission level set by the server
	 * admin, then use {@link Command#getPermissionLevel()}
	 * @return
	 *        The <b>default</b> permission level.
	 */
	public abstract int getDefaultPermissionLevel();
	
	/**
	 * Execute this command
	 * @param player
	 *              The <b>client</b> that used this command
	 * @param args
	 *           The arguments passed
	 */
	public abstract void execute(CommandExecutor player, String[] args);
	
	/**
	 * Weather this command will run outside the CommandHandler thread.
	 * Override this method to set this option
	 * @return
	 *        True if the command will run in a separate thread.
	 *        False if it will not.
	 */
	public boolean runInSeperateThread() {
		return false;
	}
	
	/**
	 * Get the true permission level for this command set by the
	 * server admin.
	 * If you want to get the default permission level, then use
	 * {@link Command#getDefaultPermissionLevel()}
	 * @return
	 *        The <b>true</b> permission level.
	 */
	public int getPermissionLevel() {
		return permission;
	}
	
	/**
	 * Set the permission level for this command
	 * @param permission
	 *                   The new permission level.
	 */
	public void setPermissionLevel(int permission) {
		this.permission = permission;
	}
	
	public boolean isOPCommand() {
		return (opcommand == 0 ? false : opcommand == -1 ? isOpCommandDefault() : true);
	}
	
	/**
	 * Sets the help for this command.
	 * Help will appear when the CommandExecutor uses /help command
	 * @param executor - the command's executor
	 */
	public abstract void help(CommandExecutor executor);

	/**
	 * Set weather this command is an OP Command or not.
	 * @param b
	 *         True if this command is an op command.
	 *         False if this command is not an op command.
	 */
	public void setOP(boolean b) {
		this.opcommand = b ? 1 : 0;
	}
}


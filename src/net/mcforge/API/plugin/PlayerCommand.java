/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.plugin;

import net.mcforge.API.CommandExecutor;
import net.mcforge.iomodel.Player;


public abstract class PlayerCommand extends Command {
	
	@Override
	public void execute(CommandExecutor e, String[] args0) {
		if (e instanceof Player) {
			Player player = (Player)e;
			execute(player, args0);
		}
	}
	
	/**
	 * Execute this command
	 * @param player
	 *              The <b>client</b> that used this command
	 * @param args
	 *           The arguments passed
	 */
	public abstract void execute(Player player, String[] args);
}

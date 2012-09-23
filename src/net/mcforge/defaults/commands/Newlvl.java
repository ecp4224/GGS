/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.defaults.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.Command;
import net.mcforge.world.LevelHandler;

public class Newlvl extends Command {
	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "newlvl";
	}

	@Override
	public boolean isOpCommand() {
		return true;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 0;
	}

	@Override
	public void execute(CommandExecutor player, String[] args) {
		if (args.length == 1) {
			short sh = 64;
			createLevel(player, args[0], sh, sh, sh);
		}
		else if (args.length == 4) {
			createLevel(player, args[0], Short.valueOf(args[1]), Short.valueOf(args[2]), Short.valueOf(args[3]));
		}
		else {
			help(player);
		}
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/newlvl <name> <w> <h> <l> - creates a new level with the specified dimensions");
		executor.sendMessage("/newlvl <name> - creates a new 64x64x64 level");
	}

	private void createLevel(CommandExecutor player, String name, short w, short h, short l) {
		LevelHandler handler = player.getServer().getLevelHandler();
		handler.loadLevels();
		if (handler.findLevel(name) == null) {
			handler.newLevel(name, w, h, l);
			player.sendMessage("Created new level: \"" + name + "\"!");
		}
		else {
			player.sendMessage("Level \"" + name + "\" already exists!");
		}
	}
}

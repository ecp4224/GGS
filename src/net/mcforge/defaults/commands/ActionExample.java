/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.defaults.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.action.Action;
import net.mcforge.API.action.ChatAction;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;

public class ActionExample extends PlayerCommand
{
	@Override
	public String[] getShortcuts()
	{
		return new String[0];
	}

	@Override
	public String getName()
	{
		return "actoinexe";
	}

	@Override
	public boolean isOpCommand()
	{
		return false;
	}

	@Override
	public int getDefaultPermissionLevel()
	{
		return 0;
	}

	public class Test extends Thread {
		public Player player;
		@Override
		public void run() {
			Action<ChatAction> a = new ChatAction();
			a.setPlayer(player);
			try {
				ChatAction c = a.waitForResponse();
				player.sendMessage("You said " + c.getMessage() + "!");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void execute(Player player, String[] args)
	{
		Test t = new Test();
		t.player = player;
		t.start();
		player.sendMessage("Say something!");
	}

	@Override
	public void help(CommandExecutor executor) {
		executor.sendMessage("/actionexample - a test command");	
	}
}

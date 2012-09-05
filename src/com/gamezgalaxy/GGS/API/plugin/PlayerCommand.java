package com.gamezgalaxy.GGS.API.plugin;

import com.gamezgalaxy.GGS.API.CommandExecutor;
import com.gamezgalaxy.GGS.iomodel.Player;


public abstract class PlayerCommand extends Command {
	
	@Override
	public void execute(CommandExecutor e, String[] args0) {
		if (e instanceof Player) {
			Player player = (Player)e;
			execute(player, args0);
		}
	}
	
	public abstract void execute(Player player, String[] args);
}

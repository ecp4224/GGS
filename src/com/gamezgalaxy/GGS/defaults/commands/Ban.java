package com.gamezgalaxy.GGS.defaults.commands;

import com.gamezgalaxy.GGS.API.plugin.Command;
import com.gamezgalaxy.GGS.iomodel.Player;
import com.gamezgalaxy.GGS.system.BanHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 8/2/12
 * Time: 11:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Ban extends Command {

	@Override
	public String[] getShortcuts() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "ban";
	}

	@Override
	public boolean isOpCommand() {
		return true;
	}

	@Override
	public int getDefaultPermissionLevel() {
		return 100;
	}

	@Override
	public void execute(Player player, String[] args) {
		BanHandler.ban(args[0]);
		//TODO Add expire date
	}

}

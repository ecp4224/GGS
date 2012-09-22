package net.mcforge.defaults.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;
import net.mcforge.system.BanHandler;

public class Ban extends PlayerCommand {

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
		if(args.length != 0){
				if(BanHandler.isBanned(args[0])){
					player.sendMessage(args[0]+ " is already banned");
					return;
				}
				if(!args[0].equalsIgnoreCase(player.username)){
					BanHandler.ban(args[0]);
					player.sendMessage("You successfully banned " + args[0]);
				}else{
					player.sendMessage("You cannot ban your self.");
				}
		}
			else 
				player.sendMessage("Correct Format: /ban <playername>");
		//TODO Add expire date
	}

	@Override
	public void help(CommandExecutor executor) {
		// TODO Auto-generated method stub
		
	}

}

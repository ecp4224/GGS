package com.gamezgalaxy.GGS.API.plugin;

import com.gamezgalaxy.GGS.server.Player;

public abstract class Command {
	
	private int permission;
	
	public abstract String[] getShortcuts();
	
	public abstract String getName();
	
	public abstract boolean isOpCommand();
	
	public abstract int getDefaultPermissionLevel();
	
	public abstract void execute(Player player, String[] args);
	
	public int getPermissionLevel() {
		return permission;
	}
	public void setPermissionLevel(int permission) {
		this.permission = permission;
	}
}

/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.API.plugin;

import com.gamezgalaxy.GGS.iomodel.Player;

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

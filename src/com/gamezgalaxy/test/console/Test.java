/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.test.console;

import com.gamezgalaxy.GGS.API.EventHandler;
import com.gamezgalaxy.GGS.API.Listener;
import com.gamezgalaxy.GGS.API.player.PlayerBlockPlaceEvent;
import com.gamezgalaxy.GGS.API.player.PlayerMoveEvent;

public class Test implements Listener {
	/*
	@EventHandler
	public void TestEvent(PlayerChatEvent event) {
		System.out.println("HI!");
	}*/
	
	@EventHandler
	public void TestEvent2(PlayerMoveEvent event) {
		if (event.getPlayer().isLoggedin && event.getPlayer().username.equals("lolwut"))
			event.Cancel(true);
	}
	
	@EventHandler
	public void TestEvent3(PlayerBlockPlaceEvent event){
		if (event.getBlock().toString().contains("Dirt")){
			//event.Cancel(true);
			//event.setBlock(Block.getBlock((byte)43));
		}
	}

}

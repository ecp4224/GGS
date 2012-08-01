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
import com.gamezgalaxy.GGS.API.browser.BrowserGETRequestEvent;
import com.gamezgalaxy.GGS.API.player.PlayerBlockPlaceEvent;
import com.gamezgalaxy.GGS.API.player.PlayerMoveEvent;
import com.gamezgalaxy.GGS.world.Block;

public class Test implements Listener {
	/*
	@EventHandler
	public void TestEvent(PlayerChatEvent event) {
		System.out.println("HI!");
	}*/
	
	@EventHandler
	public void TestEvent2(PlayerMoveEvent event) {
	}
	
	@EventHandler
	public void TestEvent3(PlayerBlockPlaceEvent event){
		event.Cancel(true);
		event.setBlock(Block.getBlock("Lava"));
	}
	
	@EventHandler
	public void TestEvent4(BrowserGETRequestEvent event) {
		if (event.getGETRequest().contains("lolwut"))
			event.setResponse("OHAIDER");
	}

}

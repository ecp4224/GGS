/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.test.console;

import java.sql.SQLException;

import com.gamezgalaxy.GGS.API.EventHandler;
import com.gamezgalaxy.GGS.API.Listener;
import com.gamezgalaxy.GGS.API.browser.BrowserGETRequestEvent;
import com.gamezgalaxy.GGS.API.player.PlayerLoginEvent;
import com.gamezgalaxy.GGS.API.player.PlayerMoveEvent;
import com.gamezgalaxy.GGS.world.Block;

public class Test implements Listener {
	/*
	@EventHandler
	public void TestEvent(PlayerChatEvent event) {
		System.out.println("HI!");
	}*/
	
	@EventHandler
	public void TestEvent2(PlayerLoginEvent event) {
		double l = Double.parseDouble((String)(event.getPlayer().getValue("THIS IS A TEST!")));
		System.out.println(l);
		l++;
		event.getPlayer().setValue("THIS IS A TEST!", l);
		try {
			event.getPlayer().saveValue("THIS IS A TEST!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void TestEvent4(BrowserGETRequestEvent event) {
		if (event.getGETRequest().contains("lolwut"))
			event.setResponse("OHAIDER");
	}

}

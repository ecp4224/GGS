package com.gamezgalaxy.test.console;

import com.gamezgalaxy.GGS.API.EventHandler;
import com.gamezgalaxy.GGS.API.Listener;
import com.gamezgalaxy.GGS.API.player.PlayerChatEvent;
import com.gamezgalaxy.GGS.API.player.PlayerMoveEvent;

public class Test implements Listener {
	
	@EventHandler
	public void TestEvent(PlayerChatEvent event) {
		System.out.println("HI!");
	}
	
	@EventHandler
	public void TestEvent2(PlayerMoveEvent event) {
		if (event.getPlayer().isLoggedin && event.getPlayer().username.equals("lolwut"))
			event.Cancel(true);
	}

}

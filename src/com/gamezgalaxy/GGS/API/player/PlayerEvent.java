package com.gamezgalaxy.GGS.API.player;

import com.gamezgalaxy.GGS.API.Event;
import com.gamezgalaxy.GGS.server.Player;

public class PlayerEvent extends Event {
	
	private Player who;
	
	public PlayerEvent(Player who) {
		this.who = who;
	}
	
	public Player getPlayer() {
		return who;
	}

}

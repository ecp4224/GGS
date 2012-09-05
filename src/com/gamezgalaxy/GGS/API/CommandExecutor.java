package com.gamezgalaxy.GGS.API;

import com.gamezgalaxy.GGS.server.Server;

public interface CommandExecutor {
	
	public void sendMessage(String message);
	
	public Server getServer();
}

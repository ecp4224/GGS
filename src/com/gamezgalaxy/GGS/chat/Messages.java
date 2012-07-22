package com.gamezgalaxy.GGS.chat;

import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class Messages {
	protected Server server;
	
	public Messages(Server server) { this.server = server; }
	
	/**
	 * Send a message to all players on the server regardless of world
	 * 
	 * @param message
	 */
	public void serverBroadcast(String message)
	{
		for (Player p : server.players)
			p.sendMessage(message);
	}
	
	/**
	 * Send a message to all players in the specified world
	 * 
	 * @param message
	 * @param world
	 */
	public void worldBroadcast(String message, String world)
	{
		for (Player p : server.players)
		{
			if(p.world == world)
				p.sendMessage(message);
		}
	}
	
	/**
	 * Send a message to a specified username
	 * 
	 * @param message
	 * @param playerName
	 */
	public void sendMessage(String message, String playerName)
	{
		for (Player p : server.players)
			if(p.username == playerName)
				p.sendMessage(message);
	}
}

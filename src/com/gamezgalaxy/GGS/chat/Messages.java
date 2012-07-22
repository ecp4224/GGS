package com.gamezgalaxy.GGS.chat;

import java.util.ArrayList;

import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.world.Level;

public class Messages {
	protected Player player;
	protected PacketManager pm;
	public ArrayList<Player> players = new ArrayList<Player>();
	
	/**
	 * Send a message to all players on the server regardless of world
	 * 
	 * @param message
	 */
	public void serverBroadcast(String message)
	{
		for (Player p : players)
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
		for (Player p : players)
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
		for (Player p : players)
			if(p.username == playerName)
				p.sendMessage(message);
	}
}

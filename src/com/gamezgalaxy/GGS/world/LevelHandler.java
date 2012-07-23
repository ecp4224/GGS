package com.gamezgalaxy.GGS.world;

import java.io.IOException;
import java.util.ArrayList;

import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class LevelHandler {
	private ArrayList<Level> levels = new ArrayList<Level>();
	
	private Server server;
	
	private Thread saver;
	
	public LevelHandler(Server server) {
		this.server = server;
		saver = new Saver();
		saver.start();
	}
	
	public Level findLevel(String name) {
		Level temp = null;
		for (int i = 0; i < levels.size(); i++) {
			if (levels.get(i).name.equalsIgnoreCase(name))
				return levels.get(i);
			if (levels.get(i).name.contains(name) && temp == null)
				temp = levels.get(i);
			else if (levels.get(i).name.contains(name) && temp != null)
				return null;
		}
		return temp;
	}
	
	public ArrayList<Player> getPlayers(Level level) {
		ArrayList<Player> temp = new ArrayList<Player>();
		for (int i = 0; i < server.players.size(); i++)
			if (server.players.get(i).getLevel() == level)
				temp.add(server.players.get(i));
		return temp;
	}
	
	public Level loadLevel(String filename) {
		Level l = null;
		try {
			l = Level.Load(filename);
		} catch (ClassNotFoundException e) {
			server.Log("ERROR LOADING LEVEL!");
			e.printStackTrace();
		} catch (IOException e) {
			server.Log("ERROR LOADING LEVEL!");
			e.printStackTrace();
		}
		if (l != null)
			levels.add(l);
		return l;
	}
	
	public void unloadLevel(Level level) {
		if (!levels.contains(level))
			return;
		//TODO Call unload and finalize method
		//TODO Move all players to main level
		levels.remove(level);
	}
	
	private class Saver extends Thread {
		
		@Override
		public void run() {
			while(server.Running) {
				for (int i = 0; i < levels.size(); i++) {
					try {
						levels.get(i).Save();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}

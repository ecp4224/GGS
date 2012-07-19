/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.server;

import java.util.ArrayList;

import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.world.Level;

public class Server {
	protected PacketManager pm;
	public ArrayList<Player> players = new ArrayList<Player>();
	public boolean Running;
	public int Port;
	public String Name;
	public String MOTD;
	public long Salt;
	public Level MainLevel;
	public Server(String Name, int Port, String MOTD) {
		this.Port = Port;
		this.Name = Name;
		this.MOTD = MOTD;
		pm = new PacketManager(this);
	}
	
	public void Start() {
		Running = true;
		Log("Starting..");
		pm.StartReading();
		Log("Generating Level..");
		MainLevel = new Level(128, 64, 128);
		MainLevel.FlatGrass();
		Log("Done!");
	}
	
	public void Log(String log) {
		System.out.println(log);
	}
}

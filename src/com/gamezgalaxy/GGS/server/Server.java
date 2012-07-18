package com.gamezgalaxy.GGS.server;

import java.util.ArrayList;

import com.gamezgalaxy.GGS.networking.PacketManager;

public class Server {
	protected PacketManager pm;
	public ArrayList<Player> players = new ArrayList<Player>();
	public boolean Running;
	public int Port;
	public String Name;
	public String MOTD;
	public long Salt;
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
	}
	
	public void Log(String log) {
		System.out.println(log);
	}
}

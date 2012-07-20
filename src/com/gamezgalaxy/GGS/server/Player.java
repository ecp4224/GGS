/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.server;

import java.net.Socket;
import java.util.ArrayList;

import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.packets.minecraft.DespawnPlayer;
import com.gamezgalaxy.GGS.networking.packets.minecraft.GlobalPosUpdate;
import com.gamezgalaxy.GGS.networking.packets.minecraft.MOTD;
import com.gamezgalaxy.GGS.networking.packets.minecraft.SpawnPlayer;
import com.gamezgalaxy.GGS.networking.packets.minecraft.TP;
import com.gamezgalaxy.GGS.world.Level;

public class Player extends IOClient {
	protected int X;
	protected int Y;
	protected int Z;
	protected byte ID;
	protected Level level;
	protected Thread levelsender;
	protected ArrayList<Player> seeable = new ArrayList<Player>();
	public String kickreason;
	public String username;
	public String mppass;
	public boolean isConnected;
	public byte ClientType; //This might be used for custom clients *hint hint*
	public int oldX;
	public int oldY;
	public int oldZ;
	public byte yaw;
	public byte pitch;
	public Ping tick = new Ping(this);
	public Player(Socket client, PacketManager pm) {
		super(client, pm);
		ID = getFreeID();
		Listen();
		pm.server.Add(tick);
	}

	public void VerifyLogin() throws InterruptedException {
		//TODO Check for real user and group and such
		username = username.trim();
		pm.server.players.add(this);
		SendWelcome();
		setLevel(pm.server.MainLevel);
		levelsender.join(); //Wait for finish
		setX(level.spawnx);
		setY(level.spawny);
		setZ(level.spawnz);
		spawnPlayer(this);
		for (Player p : pm.server.players) {
			if (p.level == level) {
				spawnPlayer(p); //Spawn p for me
				p.spawnPlayer(this); //Spawn me for p
			}
		}
	}
	
	public void sendMoTD(String topline, String bottomline) {
		MOTD m = (MOTD)(pm.getPacket("MOTD"));
		m.topLine = topline;
		m.bottomLine = bottomline;
		m.Write(this, pm.server);
	}
	
	public void updatePlayers() {
		for (Player p : seeable) {
			if (p == this)
				continue;
			GlobalPosUpdate gps = (GlobalPosUpdate)(pm.getPacket("GlobalPosUpdate"));
			gps.toupdate = p;
			gps.Write(this, p.pm.server);
		}
	}
	
	public void spawnPlayer(Player p) {
		if (seeable.contains(p))
			return;
		SpawnPlayer sp = (SpawnPlayer)(pm.getPacket((byte)0x07));
		sp.spawn = p;
		sp.Write(this, p.pm.server);
		seeable.add(p);
	}

	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		if (this.level == level)
			return;
		this.level = level;
		levelsender = new SendLevel(this);
		levelsender.start();
	}

	public boolean isLoading() {
		return levelsender != null;
	}

	//NOTE: It appears you can have the client wait on the motd screen...
	//This could be useful for future use
	public void SendWelcome() {
		pm.getPacket("Welcome").Write(this, pm.server);
	}

	public void Kick(String reason) {
		Packet p = pm.getPacket("Kick");
		this.kickreason = reason;
		p.Write(this, pm.server);
	}

	private byte getFreeID() {
		boolean found = true;
		byte toreturn = 0;
		for (int i = 0; i < 255; i++) {
			found = true;
			for (Player p : pm.server.players) {
				if (p.ID == i) {
					found = false;
					break;
				}
			}
			if (found) {
				toreturn = (byte)i;
				break;
			}
		}
		return toreturn;
	}

	public int getX() {
		return X;
	}
	public int getY() {
		return Y;
	}
	public byte getID() {
		return ID;
	}
	public int getZ() {
		return Z;
	}
	public void setX(int value) {
		oldX = X;
		X = value;
	}
	public void setY(int value) {
		oldY = Y;
		Y = value;
	}
	public void setZ(int value) {
		oldZ = Z;
		Z = value;
	}
	public void setPos(int x, int y, int z) {
		setX(x);
		setY(y);
		setZ(z);
		TP();
	}
	public void TP() {
		TP t = (TP)(pm.getPacket("TP"));
		t.pID = ID;
		t.tp = this;
		t.Write(this, pm.server);
		for (Player p : pm.server.players) {
			if (p.level == level && p != this)
				t.Write(p, p.pm.server);
		}
	}
	
	@Override
	public void CloseConnection() {
		super.CloseConnection();
		pm.server.Remove(tick);
		DespawnPlayer pa = (DespawnPlayer)(pm.getPacket((byte)0x0c));
		pa.pID = ID;
		for (Player p : pm.server.players)
			pa.Write(p, p.pm.server);
		
	}

	protected void finishLevel() {
		levelsender = null;
		setPos(50, 50, 50);
	}

	public class Ping extends Tick {

		Player p;
		public Ping(Player p) { this.p = p; }
		@Override
		public void Tick() {
			Packet pa;
			pa = pm.getPacket((byte)0x01);
			pa.Write(p, pm.server);
			pa = null;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class SendLevel extends Thread {

		Player p;
		public SendLevel(Player p) { this.p = p; }
		@Override
		public void run() {
			Packet pa;
			pa = pm.getPacket((byte)0x02);
			pa.Write(p, pm.server);
			pa = null;
			pa = pm.getPacket((byte)0x03);
			pa.Write(p, pm.server);
			pa = null;
			pa = pm.getPacket((byte)0x04);
			pa.Write(p, pm.server);
			pa = null;
			p.finishLevel();
		}
	}
}

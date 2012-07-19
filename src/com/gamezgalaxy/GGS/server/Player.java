/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.server;

import java.net.Socket;
import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.world.Level;

public class Player extends IOClient {
	protected int X;
	protected int Y;
	protected int Z;
	protected byte ID;
	protected Level level;
	public String kickreason;
	public String username;
	public String mppass;
	public byte ClientType; //This might be used for custom clients *hint hint*
	public int oldX;
	public int oldY;
	public int oldZ;
	public byte yaw;
	public byte pitch;
	public Player(Socket client, PacketManager pm) {
		super(client, pm);
		ID = getFreeID();
		Listen();
	}
	
	public void VerifyLogin() throws InterruptedException {
		//TODO Check for real user and group and such
		SendWelcome();
		Thread.sleep(10000);
		Kick("Bye!");
	}
	
	public Level getLevel() {
		return level;
	}
	public void setLevel() {
		
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
	
	

}

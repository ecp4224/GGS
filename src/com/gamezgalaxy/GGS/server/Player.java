package com.gamezgalaxy.GGS.server;

import java.net.Socket;
import java.util.Random;

import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;

public class Player extends IOClient {
	protected int X;
	protected int Y;
	protected int Z;
	public String kickreason;
	protected byte ID;
	public int oldX;
	public int oldY;
	public int oldZ;
	public byte yaw;
	public byte pitch;
	public Player(Socket client, PacketManager pm) {
		super(client, pm);
		ID = getFreeID();
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
	
	@Override
	public boolean ReadPacket(Packet packet) {
		boolean toreturn = super.ReadPacket(packet);
		if (!toreturn)
			return false;
		switch (packet.ID) {
		
		}
		return true;
		
	}
	
	

}

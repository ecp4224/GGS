/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.networking.packets.minecraft;

import java.nio.ByteBuffer;

import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.PacketType;
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class PosUpdate extends Packet {

	public PosUpdate(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
		// TODO Auto-generated constructor stub
	}
	
	public PosUpdate(PacketManager pm) {
		super("PosUpdate", (byte)0x08, pm, PacketType.Client_to_Server);
		this.length = 9;
	}

	@Override
	public void Write(IOClient player, Server server) {
	}

	@Override
	public void Handle(byte[] message, Server server, IOClient p) {
		Player player;
		if (p instanceof Player) {
			player = (Player)p;
		}
		else
			return;
		ByteBuffer bb = ByteBuffer.allocate(9);
		bb.put(message);
		//byte ID = bb.get(0);
		short X = bb.getShort(1);
		short Y = bb.getShort(3);
		short Z = bb.getShort(5);
		player.setX(X);
		player.setY(Y);
		player.setZ(Z);
		player.yaw = bb.get(7);
		player.pitch = bb.get(8);
		
	}

}

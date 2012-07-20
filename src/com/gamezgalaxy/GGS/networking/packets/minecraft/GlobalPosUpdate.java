/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.networking.packets.minecraft;

import java.io.IOException;

import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.PacketType;
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class GlobalPosUpdate extends Packet {

	public GlobalPosUpdate(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
		// TODO Auto-generated constructor stub
	}
	
	public GlobalPosUpdate(PacketManager pm) {
		super("GlobalPosUpdate", (byte)0x09, pm, PacketType.Server_to_Client);
	}

	@Override
	public void Handle(byte[] message, Server server, IOClient player) {
	}

	@Override
	public void Write(IOClient p, Server server) {
		Player player;
		if (p instanceof Player) {
			player = (Player)p;
		}
		else
			return;
		byte[] finals = new byte[7];
		finals[0] = ID;
		finals[1] = player.getID();
		finals[2] = (byte)(player.getX() - player.oldX);
		finals[3] = (byte)(player.getY() - player.oldY);
		finals[4] = (byte)(player.getZ() - player.oldZ);
		finals[5] = player.yaw;
		finals[6] = player.pitch;
		try {
			player.WriteData(finals);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

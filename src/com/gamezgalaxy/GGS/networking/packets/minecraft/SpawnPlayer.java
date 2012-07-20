/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.networking.packets.minecraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.PacketType;
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class SpawnPlayer extends Packet {

	public byte pID;
	public SpawnPlayer(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
		// TODO Auto-generated constructor stub
	}

	public SpawnPlayer(PacketManager pm) {
		super("Spawn Player", (byte)0x07, pm, PacketType.Server_to_Client);
	}

	@Override
	public void Handle(byte[] message, Server server, IOClient player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void Write(IOClient p, Server server) {
		Player player;
		if (p instanceof Player) {
			player = (Player)p;
		}
		else
			return;
		try {
			byte[] send = new byte[74];
			send[0] = ID;
			send[1] = pID;
			String name = player.username;
			while (name.length() < 64)
				name += " ";
			byte[] nameb = name.getBytes("US-ASCII");
			System.arraycopy(nameb, 0, send, 2, 64);
			System.arraycopy(HTNO((short) player.getX()), 0, send, 66, 2);
			System.arraycopy(HTNO((short) player.getY()), 0, send, 68, 2);
			System.arraycopy(HTNO((short) player.getZ()), 0, send, 70, 2);
			send[72] = player.yaw;
			send[73] = player.pitch;
			player.WriteData(send);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public byte[] HTNO(short x) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeShort(x);
		dos.flush();
		return baos.toByteArray();
	}

}

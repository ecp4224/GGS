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

import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.PacketType;
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class FinishLevelSend extends Packet {

	public FinishLevelSend(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
		// TODO Auto-generated constructor stub
	}

	public FinishLevelSend(PacketManager pm) {
		super("Finish Level Send", (byte)0x04, pm, PacketType.Server_to_Client);
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
			byte[] send = new byte[7];
			send[0] = ID;
			System.arraycopy(HTNO(player.getLevel().width), 0, send, 1, 2);
			System.arraycopy(HTNO(player.getLevel().depth), 0, send, 3, 2);
			System.arraycopy(HTNO(player.getLevel().height), 0, send, 5, 2);
			System.out.println(player.getLevel().height + ":" + player.getLevel().depth);
			player.WriteData(send);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void Handle(byte[] message, Server server, IOClient player) {
		// TODO Auto-generated method stub

	}
	public byte[] HTNO(short x) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeShort(x);
		dos.flush();
		return baos.toByteArray();
	}
}

/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.networking.packets.minecraft;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.PacketType;
import com.gamezgalaxy.GGS.server.Server;

public class MOTD extends Packet {

	public String topLine;
	public String bottomLine;
	public MOTD(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
		// TODO Auto-generated constructor stub
	}

	public MOTD(PacketManager pm) {
		super("MOTD", (byte)0x00, pm, PacketType.Server_to_Client);
	}

	@Override
	public void Write(IOClient player, Server server) {
		try {
			byte[] finals = new byte[131];
			finals[0] = ID;
			finals[1] = 0x07;
			while (topLine.length() < 64)
				topLine += " ";
			while (bottomLine.length() < 64)
				bottomLine += " ";
			byte[] name = topLine.getBytes("US-ASCII");
			byte[] motd = bottomLine.getBytes("US-ASCII");
			System.arraycopy(name, 0, finals, 2, name.length);
			System.arraycopy(motd, 0, finals, name.length + 2, motd.length);
			finals[130] = 0x00;
			player.WriteData(finals);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void Handle(byte[] message, Server server, IOClient player) {
		// TODO Auto-generated method stub

	}

}

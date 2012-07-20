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
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class Message extends Packet {

	public Message(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
		// TODO Auto-generated constructor stub
	}
	
	public Message(PacketManager pm) {
		super("Message", (byte)0x0d, pm, PacketType.Client_to_Server);
		this.length = 65;
	}

	@Override
	public void Write(IOClient p, Server server) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Handle(byte[] message, Server server, IOClient player) {
		Player p;
		if (player instanceof Player)
			p = (Player)player;
		else
			return;
		try {
			byte[] name = new byte[message.length - 1];
			System.arraycopy(message, 1, name, 0, name.length);
			String m = new String(name, "US-ASCII");
			server.Log(m);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

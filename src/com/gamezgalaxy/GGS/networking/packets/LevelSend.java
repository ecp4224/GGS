/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.networking.packets;

import java.io.IOException;

import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.PacketType;
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class LevelSend extends Packet {

	public LevelSend(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
		// TODO Auto-generated constructor stub
	}
	
	public LevelSend(PacketManager pm) {
		super("Level Send", (byte)0x03, pm, PacketType.Server_to_Client);
	}

	@Override
	public void Write(Player player, Server server) {
		byte[] levelbuff = new byte[player.getLevel().getLength() + 4];
		PacketManager.intToNetworkByteOrder(player.getLevel().getLength(), levelbuff, 0, 4);
		for (int i = 0; i < player.getLevel().getLength(); i++)
			levelbuff[i + 4] = player.getLevel().getTile(i).getVisableBlock();
	}

	@Override
	public void Handle(byte[] message, Server server, Player player) {
		// TODO Auto-generated method stub
		
	}

}

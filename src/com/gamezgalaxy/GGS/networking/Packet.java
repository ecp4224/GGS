/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.networking;

import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public abstract class Packet {
	
	public byte ID;
	
	public int length;
	
	public PacketType packetType;
	
	public PacketManager parent;
	
	public String name;
	
	public abstract void Handle(byte[] message, Server server, IOClient player);
	
	public abstract void Write(IOClient player, Server server);
	
	public Packet(String name, byte ID, PacketManager parent, PacketType packetType) {
		this.name = name;
		this.ID = ID;
		this.parent = parent;
		this.packetType = packetType;
	}

}

/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;

import com.ep.ggs.iomodel.SMPPlayer;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;


public class ClientSettings extends SMPPacket {

	public ClientSettings(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
	
	public ClientSettings(PacketManager pm) {
        this("ClientSettings", (byte)0xCC, pm);
    }
	
	
	@SuppressWarnings("unused")
	@Override
	public void handle(SMPPlayer client, Server server, DataInputStream reader) {
		try {
			//TODO: finish
			String locale = readString(reader);
			byte viewDistance = reader.readByte();
			byte chatFlags = reader.readByte();
			byte difficulty = reader.readByte();
			boolean showCape = reader.readBoolean();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(SMPPlayer client, Server server, Object... obj) { }
}

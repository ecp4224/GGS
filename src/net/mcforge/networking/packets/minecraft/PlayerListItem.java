/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class PlayerListItem extends SMPPacket {

	public PlayerListItem(String name, byte ID, PacketManager parent) {
		super(name, ID, parent);
	}
	
	public PlayerListItem(PacketManager pm) {
		this("PlayerListItem", (byte)0xC9, pm);
	}

	@Override
	public void handle(SMPPlayer client, Server server, DataInputStream reader) { }
	
	@Override
	public void write(SMPPlayer p, Server server, Object... obj) {
		if(obj.length >= 3 && obj[0] instanceof String && obj[1] instanceof Boolean && obj[2] instanceof Short) {
			ByteBuffer bb = ByteBuffer.allocate(6 + ((String)obj[0]).length() * 2);
			bb.put(this.ID);
			putMinecraftString((String)obj[0], bb);
			putBoolean((Boolean)obj[1], bb);
			bb.putShort((Short)obj[2]);
			
			try{
				p.writeData(bb.array());
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}

}

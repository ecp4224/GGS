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
import java.nio.ByteBuffer;

import com.ep.ggs.iomodel.SMPPlayer;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;


public class UseBed extends SMPPacket {

	public UseBed(String name, byte ID, PacketManager parent) {
		super(name, ID, parent);
	}
	
	public UseBed(PacketManager pm) {
		this("UseBed", (byte)0x11, pm);
	}


	@Override
	public void handle(SMPPlayer client, Server server, DataInputStream reader) { }
	
	@Override
	public void write(SMPPlayer p, Server server, Object... obj) {
		if(obj.length >= 4 && obj[0] instanceof Integer && obj[1] instanceof Integer && obj[2] instanceof Byte && obj[3] instanceof Integer) {
			ByteBuffer bb = ByteBuffer.allocate(15);
			bb.put(this.ID);
			bb.putInt((Integer)obj[0]);
			bb.put((byte)0x00); //pls java nu like Byte here
			bb.putInt((Integer)obj[1]);
			bb.put((Byte)obj[2]);
			bb.putInt((Integer)obj[3]);
			
			try{
				p.writeData(bb.array());
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
}

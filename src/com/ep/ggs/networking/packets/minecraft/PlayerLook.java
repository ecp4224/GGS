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

public class PlayerLook extends SMPPacket {

    public PlayerLook(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public PlayerLook(PacketManager pm) {
        super("PlayerLook", (byte)0x0C, pm);
    }

	@Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {        
    	try {
    		p.setOldRotation(p.getRotation());
        	p.getRotation().set(reader.readFloat(), reader.readFloat());
        	p.setOnGround(reader.readBoolean());
		}
		catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) { }

}

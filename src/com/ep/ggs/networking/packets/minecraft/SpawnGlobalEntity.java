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


public class SpawnGlobalEntity extends SMPPacket {

    public SpawnGlobalEntity(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public SpawnGlobalEntity(PacketManager pm) {
        this("SpawnGlobalEntity", (byte)0x47, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 5) {
            ByteBuffer bb;
            
            if (obj[0] instanceof Integer && obj[1] instanceof Byte && obj[2] instanceof Integer &&
                obj[3] instanceof Integer && obj[4] instanceof Integer) {
                bb = ByteBuffer.allocate(18);
                
                bb.put(ID);
                bb.putInt((Integer)obj[0]);
                bb.put((Byte)obj[1]);
                bb.putInt((Integer)obj[2]);
                bb.putInt((Integer)obj[3]);
                bb.putInt((Integer)obj[4]);
                
				try {
					player.writeData(bb.array());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
    }
}

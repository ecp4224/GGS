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


public class Respawn extends SMPPacket {

    public Respawn(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public Respawn(PacketManager pm) {
        this("Respawn", (byte)0x09, pm);
    }

    @Override
    public void handle(SMPPlayer client, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 5) {
            ByteBuffer bb;
            
            if (obj[0] instanceof Integer && obj[1] instanceof Byte && obj[2] instanceof Byte &&
                obj[3] instanceof Short && obj[4] instanceof String) {
                bb = ByteBuffer.allocate(11 + ((String)obj[4]).length() * 2);
                
                bb.put(ID);
                bb.putInt((Integer)obj[0]);
                bb.put((Byte)obj[1]);
                bb.put((Byte)obj[2]);
                bb.putShort((Byte)obj[3]);
                putMinecraftString((String)obj[4], bb);
                
                try {
                    p.writeData(bb.array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

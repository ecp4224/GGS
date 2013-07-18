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


public class EntityLookAndRelativeMove extends SMPPacket {

    public EntityLookAndRelativeMove(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public EntityLookAndRelativeMove(PacketManager pm) {
        this("EntityLookAndRelativeMove", (byte)0x21, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 6) {
            ByteBuffer bb;
            
            if (obj[0] instanceof Integer && obj[1] instanceof Byte && obj[2] instanceof Byte &&
            	obj[3] instanceof Byte &&  obj[4] instanceof Byte && obj[5] instanceof Byte) {
                bb = ByteBuffer.allocate(10);
                
                bb.put(ID);
                bb.putInt((Integer)obj[0]);
                bb.put((Byte)obj[1]);
                bb.put((Byte)obj[2]);
                bb.put((Byte)obj[3]);
                bb.put((Byte)obj[4]);
                bb.put((Byte)obj[5]);

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

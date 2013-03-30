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

public class UpdateWindowProperty extends SMPPacket {

    public UpdateWindowProperty(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public UpdateWindowProperty(PacketManager pm) {
        this("UpdateWindowProperty", (byte)0x69, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 3) {
            ByteBuffer bb;
            
            if (obj[0] instanceof Byte && obj[1] instanceof Short && obj[2] instanceof Short) {
                bb = ByteBuffer.allocate(6);
                
                bb.put(ID);
                bb.put((Byte)obj[1]);
                bb.putShort((Short)obj[1]);
                bb.putShort((Short)obj[2]);
                
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

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

public class LoginRequest extends SMPPacket {

    public LoginRequest(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public LoginRequest(PacketManager pm) {
        this("LoginRequest", (byte)0x01, pm);
    }

    @Override
    public void handle(SMPPlayer client, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer client, Server server, Object... obj) {
        if (obj.length >= 7) {
            ByteBuffer bb;
            
            if (obj[0] instanceof Integer && obj[1] instanceof String && obj[2] instanceof Byte && 
                obj[3] instanceof Byte && obj[4] instanceof Byte && obj[5] instanceof Byte && obj[6] instanceof Byte) {
                bb = ByteBuffer.allocate(12 + ((String)obj[1]).length() * 2);
                
                bb.put(ID);
                bb.putInt((Integer)obj[0]);
                putMinecraftString((String)obj[1], bb);
                bb.put((Byte)obj[2]);
                bb.put((Byte)obj[3]);
                bb.put((Byte)obj[4]);
                bb.put((Byte)obj[5]);
                bb.put((Byte)obj[6]);
                
                try {
                    client.writeData(bb.array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

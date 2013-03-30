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

public class BlockChange extends SMPPacket {

    public BlockChange(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public BlockChange(PacketManager pm) {
        this("BlockChange", (byte)0x35, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 5) {
            ByteBuffer bb;
            
            if (obj[0] instanceof Integer && obj[1] instanceof Byte && obj[2] instanceof Integer &&
                obj[3] instanceof Short && obj[4] instanceof Byte) {
                bb = ByteBuffer.allocate(13);
                
                bb.put(ID);
                bb.putInt((Integer)obj[0]);
                bb.put((Byte)obj[1]);
                bb.putInt((Integer)obj[2]);
                bb.putShort((Short)obj[3]);
                bb.put((Byte)obj[4]);

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

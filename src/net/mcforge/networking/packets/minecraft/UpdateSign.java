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

public class UpdateSign extends SMPPacket {

    public UpdateSign(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public UpdateSign(PacketManager pm) {
        this("UpdateSign", (byte)0x82, pm);
    }

    @SuppressWarnings("unused") //temp
	@Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
		try {
			int x = reader.readInt();
			short y = reader.readShort();
			int z = reader.readInt();
			String line1 = readString(reader);
			String line2 = readString(reader);
			String line3 = readString(reader);
			String line4 = readString(reader);
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >=  7) {
        	if (obj[0] instanceof Integer && obj[1] instanceof Short && obj[2] instanceof Integer &&
        		obj[3] instanceof String && obj[4] instanceof String && obj[5] instanceof String &&
        		obj[6] instanceof String) {
        		String line1 = (String)obj[3];
        		String line2 = (String)obj[4];
        		String line3 = (String)obj[5];
        		String line4 = (String)obj[6];
        		int length = (line1.length() * 2) + (line2.length() * 2) + (line3.length() * 2) + (line4.length() * 2);
                ByteBuffer bb = ByteBuffer.allocate(11 + length);
                
                bb.putInt((Integer)obj[0]);
                bb.putShort((Short)obj[1]);
                bb.putInt((Integer)obj[2]);
                putMinecraftString(line1, bb);
                putMinecraftString(line2, bb);
                putMinecraftString(line3, bb);
                putMinecraftString(line4, bb);
                
				try {
					p.writeData(bb.array());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }
}

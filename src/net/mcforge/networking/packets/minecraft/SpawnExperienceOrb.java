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

public class SpawnExperienceOrb extends SMPPacket {

    public SpawnExperienceOrb(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public SpawnExperienceOrb(PacketManager pm) {
        this("SpawnExperienceOrb", (byte)0x1A, pm);
    }

    @Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 5 && obj[0] instanceof Integer && obj[1] instanceof Integer && obj[2] instanceof Integer &&
        	obj[3] instanceof Integer && obj[4] instanceof Short) {       
			ByteBuffer bb = ByteBuffer.allocate(19);
			bb.put(this.ID);
			bb.putInt((Integer)obj[0]);
			bb.putInt((Integer)obj[1]);
			bb.putInt((Integer)obj[2]);
			bb.putInt((Integer)obj[3]);
			bb.putShort((Short)obj[4]);
			try {
				p.writeData(bb.array());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}

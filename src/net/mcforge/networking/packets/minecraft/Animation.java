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

public class Animation extends SMPPacket {

    public Animation(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public Animation(PacketManager pm) {
        this("Animation", (byte)0x12, pm);
    }

    @SuppressWarnings("unused") //temp
	@Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
		try {
			int EID = reader.readInt();
			byte animationID = reader.readByte();
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
		if (obj.length >= 2 && obj[0] instanceof Integer && obj[1] instanceof Byte) {
			ByteBuffer bb = ByteBuffer.allocate(6);
			bb.put(this.ID);
			bb.putInt((Integer) obj[0]);
			bb.put((Byte) obj[1]);
			try {
				p.writeData(bb.array());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}

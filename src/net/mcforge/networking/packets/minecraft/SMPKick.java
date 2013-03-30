/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class SMPKick extends SMPPacket {

    public SMPKick(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public SMPKick(PacketManager parent) {
        this("SMPKick", (byte)0xFF, parent);
    }

    @Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
        
    }

    @Override
    public void write(SMPPlayer client, Server server, Object... obj) {
        try {
            String reason = obj.length == 0 ? "No reason given" : (String)obj[0];
            ByteBuffer b = ByteBuffer.allocate(3 + (reason.length() * 2));
            b.put(ID);
            putMinecraftString(reason, b);
            client.writeData(b.array());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

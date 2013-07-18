/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.minecraft;

import java.io.DataInputStream;

import com.ep.ggs.iomodel.SMPPlayer;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;

public class Handshake extends SMPPacket {
    public Handshake(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public Handshake(PacketManager pm) {
        super("Hankshake", (byte)0x02, pm);
    }

    @Override
    public void handle(SMPPlayer player, Server server, DataInputStream bais) {
        try {
            byte ver = bais.readByte();
            String username = readString(bais);
            String server_host = readString(bais);
            int port = bais.readInt();
            System.out.println(ver + " : " + username + " : " + server_host + " : " + port);
            
            player.username = username;
            player.requestLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(SMPPlayer client, Server server, Object... obj) { }

}

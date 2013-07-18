/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.classicminecraft;

import java.io.IOException;

import com.ep.ggs.API.io.PacketPrepareEvent;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;


public class DespawnPlayer extends ClassicPacket {

    public DespawnPlayer(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public DespawnPlayer(PacketManager pm) {
        super("DespawnPlayer", (byte)0x0c, pm);
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {    
    }

    @Override
    public void Write(IOClient player, Server server, Object...parrams) {
        PacketPrepareEvent event = new PacketPrepareEvent(player, this, server);
        server.getEventSystem().callEvent(event);
        if (event.isCancelled())
            return;
        byte[] finals = new byte[2];
        finals[0] = ID;
        finals[1] = ((Byte)parrams[0]).byteValue();
        try {
            player.writeData(finals);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Write(IOClient player, Server servers) {
    }

}


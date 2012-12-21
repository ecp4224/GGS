/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.classicminecraft;

import java.io.IOException;

import net.mcforge.API.io.PacketPrepareEvent;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class DespawnPlayer extends Packet {

    public DespawnPlayer(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public DespawnPlayer(PacketManager pm) {
        super("DespawnPlayer", (byte)0x0c, pm);
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
        // TODO Auto-generated method stub        
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


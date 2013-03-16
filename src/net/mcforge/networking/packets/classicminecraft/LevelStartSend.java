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
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class LevelStartSend extends ClassicPacket {

    public LevelStartSend(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public LevelStartSend(PacketManager pm) {
        super("Start ClassicLevel Send", (byte)0x02, pm);
    }

    @Override
    public void Write(IOClient player, Server server) {
        PacketPrepareEvent event = new PacketPrepareEvent(player, this, server);
        server.getEventSystem().callEvent(event);
        if (event.isCancelled())
            return;
        try {
            player.writeData(new byte[] { ID });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
    }

}


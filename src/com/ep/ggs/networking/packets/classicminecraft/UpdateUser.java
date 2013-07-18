/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.classicminecraft;

import java.io.IOException;

import com.ep.ggs.iomodel.Player;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;


public class UpdateUser extends ClassicPacket {

    public UpdateUser(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public UpdateUser(PacketManager pm) {
        super("UpdateUser", (byte)0x0f, pm);
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
    }

    @Override
    public void Write(IOClient player, Server server) {
        Player p = null;
        if (player instanceof Player) {
            p = (Player)player;
        }
        else
            return;
        byte[] final1 = new byte[2];
        final1[0] = (byte)0x0f;
        final1[1] = (byte)(p.getGroup().isOP ? 0x64 : 0x00);
        try {
            player.writeData(final1);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}


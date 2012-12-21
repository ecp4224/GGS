/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.classicminecraft;

import java.io.IOException;

import net.mcforge.iomodel.Player;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class UpdateUser extends Packet {

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


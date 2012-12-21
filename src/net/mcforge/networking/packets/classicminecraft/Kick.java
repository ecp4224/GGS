/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.classicminecraft;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.mcforge.API.io.PacketPrepareEvent;
import net.mcforge.iomodel.Player;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class Kick extends Packet {

    public Kick(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public Kick(PacketManager pm) {
        super("Kick", (byte)0x0e, pm);
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
    }

    @Override
    public void Write(IOClient p, Server server) {
        PacketPrepareEvent event = new PacketPrepareEvent(p, this, server);
        server.getEventSystem().callEvent(event);
        if (event.isCancelled())
            return;
        Player player;
        if (p instanceof Player) {
            player = (Player)p;
        }
        else
            return;
        try {
            while (player.kickreason.length() < 64) {
                player.kickreason += " ";
            }
            byte[] temp = player.kickreason.getBytes("US-ASCII");
            byte[] finals = new byte[1 + temp.length];
            finals[0] = ID;
            System.arraycopy(temp, 0, finals, 1, temp.length);
            player.writeData(finals);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


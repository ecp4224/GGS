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

public class Message extends Packet {

    public Message(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public Message(PacketManager pm) {
        super("Message", (byte)0x0d, pm);
        this.length = 65;
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
            while (player.message.length() < 64) {
                player.message += " ";
            }
            byte[] temp = player.message.getBytes("US-ASCII");
            byte[] finals = new byte[2 + temp.length];
            finals[0] = ID; finals[1] = player.getID();
            System.arraycopy(temp, 0, finals, 2, temp.length);
            player.writeData(finals);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
        Player p;
        if (player instanceof Player)
            p = (Player)player;
        else
            return;
        try {
            byte[] name = new byte[message.length - 1];
            System.arraycopy(message, 1, name, 0, name.length);
            String m = new String(name, "US-ASCII").trim();
            p.recieveMessage(m);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}


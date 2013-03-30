/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.browser;

import java.io.UnsupportedEncodingException;

import net.mcforge.iomodel.Browser;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.clients.Client;
import net.mcforge.server.Server;

public class GET extends Packet {

    public GET(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
        addClientSupport(Client.BROWSER);
    }
    
    public GET(PacketManager parent) {
        this("GET", (byte)'G', parent);
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
        if (player instanceof Browser) {
            String full = "";
            try {
                full = new String(message, "US-ASCII");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String url = full.split("\\ ")[1];
            ((Browser)player).GET(url, full, server);
        }
    }

    @Override
    public void Write(IOClient player, Server server) {
    }

}


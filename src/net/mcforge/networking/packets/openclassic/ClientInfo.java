/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.networking.packets.openclassic;

import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.PacketType;
import net.mcforge.server.Server;

public class ClientInfo extends Packet {

    public ClientInfo(String name, byte ID, PacketManager parent,
            PacketType packetType) {
        super(name, ID, parent, packetType);
    }
    public ClientInfo(PacketManager parent) {
        super("Client Info", (byte)16, parent, PacketType.Client_to_Server);
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
    }

    @Override
    public void Write(IOClient player, Server server) {
    }

}


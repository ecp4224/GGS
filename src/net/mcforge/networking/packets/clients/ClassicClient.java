/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.clients;

import java.io.IOException;
import java.net.Socket;

import net.mcforge.iomodel.Player;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.IClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;

public class ClassicClient implements IClient {

    @Override
    public byte getOPCode() {
        return 0;
    }

    @Override
    public IOClient create(Socket client, PacketManager pm) {
        Player object = new Player(client, pm);
        object.setClienttype(Client.MINECRAFT_CLASSIC);
        Packet packet = pm.getPacket("Player Connect");
        if (packet == null)
            return null;
        else {
            byte[] message = new byte[packet.length];
            try {
                if(object.getInputStream().read(message) != packet.length) {
                    pm.server.Log("Bad packet...");
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            packet.Handle(message, pm.server, object);
            return object;
        }
    }

}

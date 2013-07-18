/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.clients;

import java.io.IOException;
import java.net.Socket;

import com.ep.ggs.iomodel.Player;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.IClient;
import com.ep.ggs.networking.packets.Packet;
import com.ep.ggs.networking.packets.PacketManager;


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

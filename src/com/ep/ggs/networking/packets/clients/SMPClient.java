/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.clients;

import java.net.Socket;

import com.ep.ggs.iomodel.SMPPlayer;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.DynamicPacket;
import com.ep.ggs.networking.packets.IClient;
import com.ep.ggs.networking.packets.Packet;
import com.ep.ggs.networking.packets.PacketManager;


public class SMPClient implements IClient {

    @Override
    public byte getOPCode() {
        return 2;
    }

    @Override
    public IOClient create(Socket client, PacketManager pm) {
        SMPPlayer object = new SMPPlayer(client, pm);
        object.setClienttype(Client.MINECRAFT);
        Packet packet = pm.getPacket("Hankshake");
        if (packet == null)
            return null;
        else {
            ((DynamicPacket)packet).handle(pm.server, object, object.getInputStream());
            //object.kick("This is not an SMP Server!");
            return object;
        }
    }

}

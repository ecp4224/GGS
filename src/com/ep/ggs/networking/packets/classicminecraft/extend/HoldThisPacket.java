/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.classicminecraft.extend;

import java.io.IOException;

import com.ep.ggs.API.ClassicExtension;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;


@ClassicExtension(extName = "HoldThis")
public class HoldThisPacket extends ExtendPacket {

    public HoldThisPacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public HoldThisPacket(PacketManager parent) {
        this("HoldThis", (byte)0x31, parent);
    }

    @Override
    public void WriteData(Player client, Server servers, Object... para) throws ExtensionNotSupportedException {
        if (client.hasExtension(this)) {
            byte[] array = new byte[3];
            array[0] = ID;
            array[1] = (Byte)para[0];
            array[2] = (Byte)para[1];
            try {
                client.writeData(array);
            } catch (IOException e) {
                servers.logError(e);
            }
        }
        else
            throw new ExtensionNotSupportedException(this);
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) { }

}

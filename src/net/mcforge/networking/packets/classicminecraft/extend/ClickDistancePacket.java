/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.classicminecraft.extend;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.API.ClassicExtension;
import net.mcforge.iomodel.Player;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

@ClassicExtension(extName = "ClickDistance")
public class ClickDistancePacket extends ExtendPacket {

    public ClickDistancePacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public ClickDistancePacket(PacketManager parent) {
        this("ClickDistance", (byte)0x20, parent);
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) { }

    @Override
    public void Write(IOClient client, Server servers) {
        Write(client, servers, 12); //TODO Test if this is default..?
    }

    @Override
    public void WriteData(Player p, Server servers, Object... para) throws ExtensionNotSupportedException {
        if (!p.hasExtension(this))
            throw new ExtensionNotSupportedException(this);
        ByteBuffer bf = ByteBuffer.allocate(2);
        bf.put(ID);
        bf.putShort(Short.parseShort(para[0].toString()));
        try {
            p.writeData(bf.array());
        } catch (IOException e) {
            e.printStackTrace(servers.getLoggerOutput());
        }
    }

}


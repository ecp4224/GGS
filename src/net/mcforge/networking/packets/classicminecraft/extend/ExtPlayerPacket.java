/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.classicminecraft.extend;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.mcforge.API.ClassicExtension;
import net.mcforge.iomodel.Player;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

@ClassicExtension(extName = "ExtPlayer")
public class ExtPlayerPacket extends ExtendPacket {

    public ExtPlayerPacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public ExtPlayerPacket(PacketManager packetManager) {
        this("ExtPlayer", (byte)0x39, packetManager);
    }

    @Override
    public void WriteData(Player client, Server servers, Object... para) //Client is the spawner, para[0] is the other player
            throws ExtensionNotSupportedException {
        if (client.hasExtension(this)) {
            byte[] array = new byte[66];
            array[0] = ID;
            array[1] = ((Player)para[0]).getID();
            try {
                String cname = ((Player)para[0]).getClientName();
                while (cname.length() < 64)
                    cname += " ";
                byte[] name = cname.getBytes("US-ASCII");
                System.arraycopy(name, 0, array, 2, name.length);
                client.writeData(array);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace(servers.getLoggerOutput());
            } catch (IOException e) {
                e.printStackTrace(servers.getLoggerOutput());
            }
        }
        else
            throw new ExtensionNotSupportedException(this);
        
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
        // TODO Auto-generated method stub
        
    }

}
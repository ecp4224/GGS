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

@ClassicExtension(extName = "ExtRemovePlayerName")
public class ExtRemovePlayerNamePacket extends ExtendPacket {

    public ExtRemovePlayerNamePacket(String name, byte ID,
            PacketManager parent) {
        super(name, ID, parent);
    }
    
    public ExtRemovePlayerNamePacket(PacketManager parent) {
        this("ExtRemovePlayerName", (byte)0x35, parent);
    }

    @Override
    public void WriteData(Player client, Server servers, Object... para)
            throws ExtensionNotSupportedException {
        if (client.hasExtension(this)) {
            byte[] array = new byte[65];
            array[0] = ID;
            String name = ((Player)para[0]).getName();
            try {
                byte[] bname = name.getBytes("US-ASCII");
                System.arraycopy(bname, 0, array, 1, bname.length);
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
    public void Handle(byte[] message, Server server, IOClient player) { }

}
/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.clients;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import net.mcforge.iomodel.SimpleIOClient;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.IClient;
import net.mcforge.networking.packets.PacketManager;

public class MCServerList implements IClient {
    
    private static final char COLOR_CHAR = '\u00A7';

    @Override
    public byte getOPCode() {
        return (byte)0xFE;
    }

    @Override
    public IOClient create(Socket client, PacketManager pm) {
        SimpleIOClient ic = new SimpleIOClient(client, pm);
        DataOutputStream dos = new DataOutputStream(ic.getOutputStream());
        try {
            dos.write((byte)0xFF);
            String data = COLOR_CHAR + "1\0\51\0\1.4.7\0" + pm.server.MOTD + "\0" + pm.server.getSMPPlayers().size() + "\0" + pm.server.MaxPlayers;
            dos.writeShort((short)data.length());
            dos.writeChars(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        
    }

}

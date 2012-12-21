/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.classicminecraft.extend;

import net.mcforge.iomodel.Player;
import net.mcforge.networking.ClientType;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public abstract class ExtendPacket extends Packet {

    public ExtendPacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
        parent.server.getPluginHandler().addExtension(this);
    }
    
    @Override
    public void Write(IOClient client, Server servers, Object...para) {
        Player p = (client instanceof Player ? (Player)client : null);
        if (p == null || p.client != ClientType.Extend_Classic)
            return;
        try {
            WriteData(p, servers, para);
        } catch (ExtensionNotSupportedException e) {
            e.printStackTrace(servers.getLoggerOutput());
        }
    }
    
    @Override
    public void Write(IOClient client, Server servers) {
        Player p = (client instanceof Player ? (Player)client : null);
        if (p == null || p.client != ClientType.Extend_Classic)
            return;
        try {
            WriteData(p, servers);
        } catch (ExtensionNotSupportedException e) {
            e.printStackTrace(servers.getLoggerOutput());
        }
    }
    
    public abstract void WriteData(Player client, Server servers, Object...para) throws ExtensionNotSupportedException;

}


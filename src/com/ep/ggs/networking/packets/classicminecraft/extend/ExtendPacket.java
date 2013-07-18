/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.classicminecraft.extend;

import com.ep.ggs.iomodel.Player;
import com.ep.ggs.networking.ClassicClientType;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.Packet;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;

public abstract class ExtendPacket extends Packet {

    public ExtendPacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
        parent.server.getPluginHandler().addExtension(this);
    }
    
    @Override
    public void Write(IOClient client, Server servers, Object...para) {
        Player p = (client instanceof Player ? (Player)client : null);
        if (p == null || p.client != ClassicClientType.Extend_Classic)
            return;
        try {
            WriteData(p, servers, para);
        } catch (ExtensionNotSupportedException e) {
            servers.logError(e);
        }
    }
    
    @Override
    public void Write(IOClient client, Server servers) {
        Player p = (client instanceof Player ? (Player)client : null);
        if (p == null || p.client != ClassicClientType.Extend_Classic)
            return;
        try {
            WriteData(p, servers);
        } catch (ExtensionNotSupportedException e) {
            servers.logError(e);
        }
    }
    
    public abstract void WriteData(Player client, Server servers, Object...para) throws ExtensionNotSupportedException;

}


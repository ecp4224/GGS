/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.classicminecraft;

import com.ep.ggs.networking.packets.Packet;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.networking.packets.clients.Client;

public abstract class ClassicPacket extends Packet {

    public ClassicPacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
        super.addClientSupport(Client.MINECRAFT_CLASSIC);
    }

}

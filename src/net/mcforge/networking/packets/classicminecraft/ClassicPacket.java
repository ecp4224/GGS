/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.classicminecraft;

import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.clients.Client;

public abstract class ClassicPacket extends Packet {

    public ClassicPacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
        super.addClientSupport(Client.MINECRAFT_CLASSIC);
    }

}

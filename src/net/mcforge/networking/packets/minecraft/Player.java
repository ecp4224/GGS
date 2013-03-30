/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class Player extends SMPPacket {

    public Player(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public Player(PacketManager pm) {
        super("Player", (byte)0x0A, pm);
    }

	@Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {        
    	try {
        	boolean onGround = reader.readBoolean();
			p.setOnGround(onGround);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) { }

}

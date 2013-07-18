/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;

import com.ep.ggs.iomodel.SMPPlayer;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;

public class PlayerPosition extends SMPPacket {

    public PlayerPosition(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public PlayerPosition(PacketManager pm) {
        super("PlayerPosition", (byte)0x0B, pm);
    }

	@Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {        
    	try {
    		p.setOldLocation(p.getLocation());
        	double x = reader.readDouble();
        	double y = reader.readDouble();
        	double stance = reader.readDouble();
        	double z = reader.readDouble();
        	boolean onGround = reader.readBoolean();
        	
        	if ((stance - y) < 0.1d || (stance - y) > 1.65d) {
        		p.kick("Illegal Stance");
        	}
        	
        	if ((Math.abs(x) >= Math.abs(p.getLocation().getX()) + 100) || //TODO: possibly add customization for this number and an option for if it will be checked at all
        		(Math.abs(y) >= Math.abs(p.getLocation().getY()) + 100) ||
        		(Math.abs(z) >= Math.abs(p.getLocation().getZ()) + 100)) {
        		p.kick("You moved too quickly");
        	}
        	
        	if (Math.abs(x) > 3.2E7d || Math.abs(y) > 3.2E7d) {
        		p.kick("Illegal Position");
        	}
        	p.getLocation().setX(x);
        	p.getLocation().setY(y);
        	p.getLocation().setZ(z);
        	p.setStance(stance);
        	p.setOnGround(onGround);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) { }

}

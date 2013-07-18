/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.classicminecraft;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.ep.ggs.API.ClassicExtension;
import com.ep.ggs.API.player.PlayerMoveEvent;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;


@ClassicExtension(extName = "HeldBlock")
public class PosUpdate extends ClassicPacket {

    public PosUpdate(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
        parent.server.getPluginHandler().addExtension(this);
        this.length = 9;
    }
    
    public PosUpdate(PacketManager pm) {
        this("PosUpdate", (byte)0x08, pm);
    }

    @Override
    public void Write(IOClient player, Server server) {
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient p) {
        Player player;
        if (p instanceof Player) {
            player = (Player)p;
        }
        else
            return;
        ByteBuffer bb = ByteBuffer.allocate(9);
        bb.put(message);
        byte ID = bb.get(0);
        short X = bb.getShort(1);
        short Y = bb.getShort(3);
        short Z = bb.getShort(5);
        player.yaw = bb.get(7);
        player.pitch = bb.get(8);
        if (player.hasExtension(this))
            player.setHoldingBlock(ID);
        player.setX(X);
        player.setY(Y);
        player.setZ(Z);
        PlayerMoveEvent event = new PlayerMoveEvent(player, X, Y, Z);
        server.getEventSystem().callEvent(event);
        if (event.isCancelled()) {
            player.setPos(player.oldX, player.oldY, player.oldZ, player.oldyaw, player.oldpitch);
            return;
        }
        try {
            player.updatePos();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

}


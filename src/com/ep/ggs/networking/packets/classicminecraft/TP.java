/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.classicminecraft;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.ep.ggs.API.io.PacketPrepareEvent;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;


public class TP extends ClassicPacket {
    public TP(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public TP(PacketManager pm) {
        super("TP", (byte)0x08, pm);
    }
    
    public byte[] toSend(Player toteleport) {
        ByteBuffer bb = ByteBuffer.allocate(10);
        bb.put(ID);
        bb.put(toteleport.getID());
        bb.putShort(toteleport.getX());
        bb.putShort(toteleport.getY());
        bb.putShort(toteleport.getZ());
        bb.put(toteleport.yaw);
        bb.put(toteleport.pitch);
        return bb.array();
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
    }

    @Override
    public void Write(IOClient p, Server server, Object...parma) { //PLAYER IS THE PLAYER RECIEVING THE DATA, TP IS THE PERSON TP'ING
        PacketPrepareEvent event = new PacketPrepareEvent(p, this, server);
        server.getEventSystem().callEvent(event);
        if (event.isCancelled())
            return;
        Player player;
        Player tp = (Player)parma[0];
        if (p instanceof Player) {
            player = (Player)p;
        }
        else
            return;
        try {
            ByteBuffer bb = ByteBuffer.allocate(10);
            bb.put(ID);
            bb.put((tp == p) ? (byte)-1 : tp.getID());
            bb.putShort(tp.getX());
            bb.putShort(tp.getY());
            bb.putShort(tp.getZ());
            bb.put(tp.yaw);
            bb.put(tp.pitch);
            player.writeData(bb.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public byte[] HTNO(short x) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeShort(x);
        dos.flush();
        return baos.toByteArray();
    }

    @Override
    public void Write(IOClient player, Server servers) {
    }

}


/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.minecraft;

import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.API.io.PacketPrepareEvent;
import net.mcforge.iomodel.Player;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.PacketType;
import net.mcforge.server.Server;
import net.mcforge.world.PlaceMode;

/**
 * This packet will handle both the 0x05 packet and the 0x06 packet
 * @author Eddie
 *
 */
public class SetBlock extends Packet {
	public SetBlock(String name, byte ID, PacketManager parent, PacketType packetType) {
		super(name, ID, parent, packetType);
	}
	public SetBlock(PacketManager pm) {
		super("SetBlock", (byte)0x05, pm, PacketType.Client_to_Server);
		this.length = 8;
	}
	@Override
	public void Write(IOClient player, Server server, Object...parma) {
		PacketPrepareEvent event = new PacketPrepareEvent(player, this, server);
		server.getEventSystem().callEvent(event);
		if (event.isCancelled())
			return;
		short X = ((Short)parma[0]).shortValue();
		short Y = ((Short)parma[1]).shortValue();
		short Z = ((Short)parma[2]).shortValue();
		byte block = ((Byte)parma[3]).byteValue();
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.put((byte)0x06);
		bb.putShort(X);
		bb.putShort(Y);
		bb.putShort(Z);
		bb.put(block);
		try {
			player.WriteData(bb.array());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void Handle(byte[] message, Server server, IOClient player) {
		Player p = null;
		if (player instanceof Player)
			p = (Player)player;
		else
			return;
		ByteBuffer bb = ByteBuffer.allocate(9);
		bb.put(message);
		short X = bb.getShort(0);
		short Y = bb.getShort(2);
		short Z = bb.getShort(4);
		PlaceMode pm = PlaceMode.parse(bb.get(6));
		byte block = bb.get(7);
		p.HandleBlockChange(X, Y, Z, pm, block);
	}
	@Override
	public void Write(IOClient player, Server servers) {
	}

}

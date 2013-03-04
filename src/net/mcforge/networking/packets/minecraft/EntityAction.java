package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class EntityAction extends SMPPacket {

	public EntityAction(String name, byte ID, PacketManager parent) {
		super(name, ID, parent);
	}

	public EntityAction(PacketManager pm) {
		this("EntityAction", (byte) 0x13, pm);
	}

	@SuppressWarnings("unused") // temp
	@Override
	public void handle(SMPPlayer player, Server server, DataInputStream reader) {
		try {
			int EID = reader.readInt();
			byte actionID = reader.readByte();
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void write(SMPPlayer p, Server server, Object... obj) {
	}
}

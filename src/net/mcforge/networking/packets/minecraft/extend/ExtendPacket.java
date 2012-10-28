package net.mcforge.networking.packets.minecraft.extend;

import net.mcforge.iomodel.Player;
import net.mcforge.networking.ClientType;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.PacketType;
import net.mcforge.server.Server;

public abstract class ExtendPacket extends Packet {

	public ExtendPacket(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
	}
	
	@Override
	public void Write(IOClient client, Server servers, Object...para) {
		Player p = (client instanceof Player ? (Player)client : null);
		if (p == null || p.client != ClientType.Extend_Classic)
			return;
		WriteData(p, servers, para);
	}
	
	@Override
	public void Write(IOClient client, Server servers) {
		Player p = (client instanceof Player ? (Player)client : null);
		if (p == null || p.client != ClientType.Extend_Classic)
			return;
		WriteData(p, servers);
	}
	
	public abstract void WriteData(Player client, Server servers, Object...para);

}


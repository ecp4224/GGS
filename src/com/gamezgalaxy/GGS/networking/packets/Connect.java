package com.gamezgalaxy.GGS.networking.packets;

import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.PacketType;
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class Connect extends Packet {

	public Connect(String name, byte ID, PacketManager parent, PacketType packetType) {
		super(name, ID, parent, packetType);
	}
	public Connect(PacketManager pm) {
		super("Player Connect", (byte)0x00, pm, PacketType.Client_to_Server);
	}

	@Override
	public byte[] Read(Server server, Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Write(Player player, Server server) {
		// TODO Auto-generated method stub
		
	}

}

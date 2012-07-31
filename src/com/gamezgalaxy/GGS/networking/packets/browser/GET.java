package com.gamezgalaxy.GGS.networking.packets.browser;

import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.PacketType;
import com.gamezgalaxy.GGS.server.Server;

public class GET extends Packet {

	public GET(String name, byte ID, PacketManager parent, PacketType packetType) {
		super(name, ID, parent, packetType);
	}
	
	public GET(PacketManager parent) {
		this("GET", (byte)'G', parent, PacketType.Client_to_Server);
	}

	@Override
	public void Handle(byte[] message, Server server, IOClient player) {
	}

	@Override
	public void Write(IOClient player, Server server) {
	}

}

package com.gamezgalaxy.GGS.networking.packets;

import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.PacketType;
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class TP extends Packet {

	public TP(String name, byte ID, PacketManager parent, PacketType packetType) {
		super(name, ID, parent, packetType);
		// TODO Auto-generated constructor stub
	}
	
	public TP(PacketManager pm) {
		super("TP", (byte)0x08, pm, PacketType.Server_to_Client);
	}

	@Override
	public void Handle(byte[] message, Server server, Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Write(Player player, Server server) {
		// TODO Auto-generated method stub
		
	}

}

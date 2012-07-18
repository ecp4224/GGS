package com.gamezgalaxy.GGS.networking.packets;

import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.networking.PacketType;
import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public class PosUpdate extends Packet {

	public PosUpdate(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
		// TODO Auto-generated constructor stub
	}
	
	public PosUpdate(PacketManager pm) {
		super("PosUpdate", (byte)0x08, pm, PacketType.Client_to_Server);
	}

	@Override
	public void Write(Player player, Server server) {
	}

	@Override
	public void Handle(byte[] message, Server server, Player player) {
		short X = (short)((message[3] << 8) | message[2]);
		short Y = (short)((message[5] << 8) | message[4]);
		short Z = (short)((message[7] << 8) | message[6]);
		player.setX(X);
		player.setY(Y);
		player.setZ(Z);
	}

}

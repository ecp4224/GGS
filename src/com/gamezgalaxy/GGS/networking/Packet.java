package com.gamezgalaxy.GGS.networking;

import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;

public abstract class Packet {
	
	public byte ID;
	
	public int lenght;
	
	public PacketType packetType;
	
	public PacketManager parent;
	
	public String name;
	
	public abstract byte[] Read(Server server, Player player);
	
	public abstract void Write(Player player, Server server);
	
	public Packet(String name, byte ID, PacketManager parent, PacketType packetType) {
		this.name = name;
		this.ID = ID;
		this.parent = parent;
		this.packetType = packetType;
	}

}

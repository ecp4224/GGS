package com.gamezgalaxy.GGS.server;

import java.net.Socket;

import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.Packet;
import com.gamezgalaxy.GGS.networking.PacketManager;

public class Player extends IOClient {

	public Player(Socket client, PacketManager pm) {
		super(client, pm);
	}
	
	@Override
	public boolean ReadPacket(Packet packet) {
		boolean toreturn = super.ReadPacket(packet);
		if (!toreturn)
			return false;
		switch (packet.ID) {
		
		}
		return true;
		
	}
	
	

}

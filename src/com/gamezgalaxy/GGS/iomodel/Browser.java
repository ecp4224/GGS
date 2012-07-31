package com.gamezgalaxy.GGS.iomodel;

import java.net.Socket;

import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.PacketManager;

public class Browser extends IOClient {

	public Browser(Socket client, PacketManager pm) {
		super(client, pm);
	}

}

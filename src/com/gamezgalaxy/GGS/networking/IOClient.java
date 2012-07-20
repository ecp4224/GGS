/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.networking;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import com.gamezgalaxy.GGS.server.Player;

public class IOClient {
	protected Socket client;
	
	protected PrintStream writer;
	
	protected DataInputStream reader;
	
	protected Thread readerthread;
	
	protected PacketManager pm;
	
	public IOClient(Socket client, PacketManager pm) {
		this.client = client;
		this.pm = pm;
		try {
			writer = new PrintStream(client.getOutputStream());
			//writer.flush();
			reader = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			pm.server.Log("Error");
			e.printStackTrace();
		}
	}
	
	public void Listen() {
		readerthread = new Reader(this);
		readerthread.start();
		pm.server.Log("Listening..");
	}
	
	public void CloseConnection() {
		try {
			pm.server.Log("Closing connection");
			writer.close();
			reader.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void WriteData(byte[] data) throws IOException {
		writer.write(data);
		writer.flush();
	}
	
	public class Reader extends Thread {
		IOClient client;
		
		public Reader(IOClient client) { this.client = client; }
		@Override
		public void run() {
			while (pm.server.Running && client.client.isConnected()) {
				try {
					byte opCode = reader.readByte();
					Packet packet = pm.getPacket(opCode);
					if (packet == null) {
						pm.server.Log("Client sent " + opCode);
						pm.server.Log("How do..?");
						continue;
					}
					byte[] message = new byte[packet.length];
					reader.read(message);
					if (message.length < packet.length) {
						pm.server.Log("Bad packet..");
						continue;
					}
					packet.Handle(message, pm.server, (Player)client);
				} catch (IOException e) {
					CloseConnection();
					break;
				}
				catch (Exception e) {
					if (client instanceof Player)
						((Player)client).Kick("ERROR!");
					CloseConnection();
					e.printStackTrace();
					break;
				}
			}
		}
	}
}

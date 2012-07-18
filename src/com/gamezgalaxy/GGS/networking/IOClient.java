package com.gamezgalaxy.GGS.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOClient {
	protected Socket client;
	
	protected ObjectOutputStream writer;
	
	protected ObjectInputStream reader;
	
	protected Thread readerthread;
	
	protected PacketManager pm;
	
	public IOClient(Socket client, PacketManager pm) {
		this.client = client;
		this.pm = pm;
		try {
			writer = new ObjectOutputStream(client.getOutputStream());
			reader = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void CloseConnection() {
		try {
			writer.close();
			reader.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Read the data the client sends
	 * @param packet
	 * @return if returns true, then the read was successful, otherwise it failed.
	 */
	public boolean ReadPacket(Packet packet) {
		if (packet == null) {
			//TODO Kick the client
			CloseConnection();
			return false;
		}
		return true;
	}
	
	public class Reader extends Thread {
		
		@Override
		public void run() {
			while (pm.server.Running) {
				try {
					byte opCode = reader.readByte();
					
					
				} catch (IOException e) {
					CloseConnection();
					//e.printStackTrace();
					break;
				}
			}
		}
	}
}

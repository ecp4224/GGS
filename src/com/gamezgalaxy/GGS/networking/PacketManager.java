package com.gamezgalaxy.GGS.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.gamezgalaxy.GGS.networking.packets.*;
import com.gamezgalaxy.GGS.server.Server;

public class PacketManager {
	
	protected Packet[] packets = new Packet[] {
		new Connect(this),
		new DespawnPlayer(this),
		new FinishLevelSend(this),
		new GlobalPosUpdate(this),
		new Kick(this),
		new LevelSend(this),
		new LevelStartSend(this),
		new Message(this),
		new Ping(this),
		new PosUpdate(this),
		new ServerMessage(this),
		new SetBlock(this),
		new SpawnPlayer(this),
		new TP(this),
		new UpdateUser(this),
		new Welcome(this)
	};
	
	protected ServerSocket serverSocket;
	
	protected Thread reader;
	
	public Server server;
	
	public PacketManager(Server instance) {
		this.server = instance;
		try {
			serverSocket = new ServerSocket(this.server.Port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Packet getPacket(byte opCode) {
		for (Packet p : packets) {
			if (p.ID == opCode)
				return p;
		}
		return null;
	}
	
	public void StartReading() {
		reader = new Read();
		reader.start();
	}
	
	public static long ConvertToInt32(byte[] array) {
		long toreturn = 0;
		for (int i = 0; i < array.length; i++) {
			toreturn += ((long) array[i] & 0xffL) << (8 * i);
		}
		return toreturn;
	}
	public static short INT_little_endian_TO_big_endian(short i)
	{
		return(short)(((i&0xff)<<24)+((i&0xff00)<<8)+((i&0xff0000)>>8)+((i>>24)&0xff));
	}
	
	public class Read extends Thread {
		
		@Override
		public void run() {
			Socket connection = null;
			while (server.Running) {
				try {
					connection = serverSocket.accept();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}

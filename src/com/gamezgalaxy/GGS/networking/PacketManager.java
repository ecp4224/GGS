package com.gamezgalaxy.GGS.networking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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

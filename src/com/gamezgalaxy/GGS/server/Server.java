/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.server;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.gamezgalaxy.GGS.networking.PacketManager;
import com.gamezgalaxy.GGS.world.Level;

public class Server {
	protected PacketManager pm;
	protected Lock lock = new ReentrantLock();
	protected ArrayList<Tick> ticks = new ArrayList<Tick>();
	protected Thread tick;
	public ArrayList<Player> players = new ArrayList<Player>();
	public boolean Running;
	public int Port;
	public int MaxPlayers;
	public String Name;
	public String MOTD;
	public String Salt;
	public Level MainLevel;
	public Server(String Name, int Port, String MOTD) {
		this.Port = Port;
		this.Name = Name;
		this.MOTD = MOTD;
		pm = new PacketManager(this);
		tick = new Ticker();
	}

	public void Start() {
		Running = true;
		Log("Starting..");
		pm.StartReading();
		Log("Generating Level..");
		MainLevel = new Level((short)64, (short)64, (short)64);
		MainLevel.FlatGrass();
		tick.start();
		Log("Done!");
		Log("Generating salt");
		SecureRandom sr = null;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < 100; i++) {
			byte[] seedb = new byte[16];
			sr.nextBytes(seedb);
			Salt = new sun.misc.BASE64Encoder().encode(seedb);
			if (new Random().nextDouble() < .3)
				break;
		}
		Log("SALT: " + Salt);
	}

	public void Stop() throws InterruptedException {
		Running = false;
		tick.join();
	}

	public void Log(String log) {
		System.out.println(log);
	}

	public  void Add(Tick t) {
		synchronized(ticks) {
			if (!ticks.contains(t))
				ticks.add(t);
		}
	}

	public void Remove(Tick t) {
		synchronized(ticks) {
			if (ticks.contains(t))
				ticks.remove(t);
		}
	}

	public class Ticker extends Thread {

		@Override
		public void run() {
			while (Running) {
				synchronized(ticks) {
					for (Tick t : ticks) {
						t.Tick();
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public void UpdatePos() {
		for (Player p : players)
			p.updatePlayers();
	}

	public void sendMessage(String message) {
		for (Player p : players)
			p.sendMessage(message);
	}
}

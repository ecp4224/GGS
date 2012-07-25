/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.system.heartbeat;

import java.util.ArrayList;

import com.gamezgalaxy.GGS.server.Server;

public class Beat {
	private Server server;

	private ArrayList<Heart> hearts = new ArrayList<Heart>();

	private Thread beater;
	
	private boolean running;

	public Beat(Server server) {
		this.server = server;
	}

	public void addHeart(Heart h) {
		synchronized(hearts) {
			if (!hearts.contains(h))
				hearts.add(h);
		}
	}
	public void removeHeart(Heart h) {
		synchronized(hearts) {
			if (hearts.contains(h))
				hearts.remove(h);
		}
	}
	public void start() {
		if (running)
			return;
		running = true;
		beater = new Beater(this);
		beater.start();
	}
	public void stop() {
		if (!running)
			return;
		running = false;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public ArrayList<Heart> getHearts() {
		return hearts;
	}

	public void setHearts(ArrayList<Heart> hearts) {
		this.hearts = hearts;
	}

	public Thread getBeater() {
		return beater;
	}

	public void setBeater(Thread beater) {
		this.beater = beater;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}

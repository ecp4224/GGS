package com.gamezgalaxy.GGS.system.heartbeat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
		beater = new Beater();
		beater.start();
	}
	public void stop() {
		if (!running)
			return;
		running = false;
		try {
			beater.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class Beater extends Thread {

		@Override
		public void run() {
			URL url;
			HttpURLConnection connection = null;
			BufferedReader reader;
			byte[] data;
			while (running) {
				synchronized(hearts) {
					for (Heart h : hearts) {
						try {
							url = new URL(h.getURL() + "?" + h.Prepare(server));
							connection = (HttpURLConnection)url.openConnection();
							data = h.Prepare(server).getBytes();
							connection.setDoOutput(true);
							connection.setRequestProperty("Content-Lenght", String.valueOf(data.length));
							connection.setUseCaches(false);
							connection.setDoInput(true);
							connection.setDoOutput(true);
							connection.connect();
							try {
								reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
								try {
									h.onPump(reader, server);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							if (connection != null)
								connection.disconnect();
						}
					}
				}
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

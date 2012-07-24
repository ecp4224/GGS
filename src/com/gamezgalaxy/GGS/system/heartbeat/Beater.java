package com.gamezgalaxy.GGS.system.heartbeat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/22/12
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Beater extends Thread {

	public Beater(Beat beat)
	{
		this.beat = beat;
	}

	private final Beat beat;

	@Override
	public void run() {
		URL url;
		HttpURLConnection connection = null;
		BufferedReader reader;
		byte[] data;
		while (beat.isRunning()) {
			synchronized(beat.getHearts()) {
				for (Heart h : beat.getHearts()) {
					try {
						URL u = new URL("http://www.minecraft.net/");
						HttpURLConnection con = (HttpURLConnection)u.openConnection();
						con.connect();

						if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
						{
							url = new URL(h.getURL() + "?" + h.Prepare(beat.getServer()));
							connection = (HttpURLConnection)url.openConnection();
							data = h.Prepare(beat.getServer()).getBytes();
							connection.setDoOutput(true);
							connection.setRequestProperty("Content-Lenght", String.valueOf(data.length));
							connection.setUseCaches(false);
							connection.setDoInput(true);
							connection.setDoOutput(true);
							connection.connect();
							try {
								reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
								try {
									h.onPump(reader, beat.getServer());
								} catch (Exception e) {
									e.printStackTrace();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						//e.printStackTrace();
						System.out.println("Unable to connect to minecraft.net!");
						try {
							beat.getServer().Stop();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
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
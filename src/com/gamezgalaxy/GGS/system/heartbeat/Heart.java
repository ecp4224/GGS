package com.gamezgalaxy.GGS.system.heartbeat;

import java.io.BufferedReader;
import java.io.IOException;

import com.gamezgalaxy.GGS.server.Server;

public abstract class Heart {

	/**
	 * @return
	 */
	public abstract String Prepare(Server server);
	
	public abstract String getURL();

	public String onPump(BufferedReader rdr, Server server) throws IOException {
		String url = "";
		String line = "";
		try {
			while (line != null) {
				line = rdr.readLine();
				if (line != null)
					url = line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rdr.close();
		}
		return url;
	}
}

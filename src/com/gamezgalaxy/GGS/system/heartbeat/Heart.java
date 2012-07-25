/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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

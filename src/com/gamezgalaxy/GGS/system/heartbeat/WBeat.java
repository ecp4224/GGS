/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.system.heartbeat;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;

import com.gamezgalaxy.GGS.server.Server;

public class WBeat extends Heart {

	@Override
	public String Prepare(Server server) {
		return "salt=" + server.Salt +
				"&users=" + server.players.size() +
				"&alt=" + server.altName +
				"&desc=" + server.description +
				"&flags=" + server.flags;
	}
	
	@Override
	public String getURL() {
		return "http://direct.worldofminecraft.com/hb.php";
	}
}

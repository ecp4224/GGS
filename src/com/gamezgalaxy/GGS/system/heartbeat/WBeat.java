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

	@Override
	public String onPump(BufferedReader rdr, Server server) throws IOException
	{
		String data1 = "mc://localhost/secretusernameoflife/" + server.Salt;
		{
			StringSelection data2 = new StringSelection(data1);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(data2, data2);
		}

		server.Log("Local Direct URL: " + data1);

		return null;
	}
}

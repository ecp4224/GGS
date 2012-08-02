/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.server;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConsoleCommands extends Thread
{
	public ConsoleCommands(Server server)
	{
		this.server = server;
	}

	private Server server;

	@Override
	public void run()
	{
		while(true)
		{
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			String s = null;
			try {
				s = bufferRead.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			List<String> list = new ArrayList<String>();
			Collections.addAll(list, s.split(" "));

			String[] args = new String[list.size()];

			for(int i = 1; i < list.size(); i++)
			{
				args[i - 1] = list.get(i);
			}

			String command = s.split(" ")[0];

			server.getCommandHandler().execute(null, command, args);
		}
	}
}

package com.gamezgalaxy.GGS.server;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/23/12
 * Time: 11:15 PM
 * To change this template use File | Settings | File Templates.
 */
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

			if(command.equals("stop")) {
				try {
					server.Stop();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else if(command.equals("ban")) {
				if(args.length == 2)
				{
					try {
						FileWriter out = new FileWriter("properties/banned.txt", true);

						out.write(args[0] + "\n");

						out.flush();
						out.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else if(command.equals("unban")) {
				if(args.length == 2)
				{
					server.removeLineFromFile("properties/banned.txt", args[0]);
				}
			}
		}
	}
}

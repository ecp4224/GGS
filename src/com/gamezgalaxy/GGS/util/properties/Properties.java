package com.gamezgalaxy.GGS.util.properties;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.gamezgalaxy.GGS.server.Server;

public class Properties {
	private static ArrayList<String> settings = new ArrayList<String>();
	
	public static void init(Server server) {
		if (!new File("properties").exists())
			new File("properties").mkdir();
		if (!new File("properties/" + server.configpath).exists())
			makeDefaults(server.configpath, server);
		else {
			try {
				load(server.configpath);
			} catch (IOException e) {
				server.Log("ERROR LOADING CONFIG!");
				e.printStackTrace();
			}
		}
		
	}
	
	public static void makeDefaults(String filename, Server server) {
		//TODO Fill in all defaults
		server.Log("System config not found..creating..");
		addSetting("Server-Name", "[GGS] Default Server");
		addSetting("WOM-Alternate-Name", "[GGS] Default Server");
		addSetting("MOTD", "Welcome!");
		addSetting("Port", 25565);
		addSetting("Max-Players", 30);
		addSetting("Public", true);
		addSetting("WOM-Server-description", "A server");
		addSetting("WOM-Server-Flags", "[GGS]");
		addSetting("MainLevel", "levels/Main.ggs");
		try {
			save(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void save(String filename) throws IOException {
		if (new File("properties/" + filename).exists())
			new File("properties/" + filename).delete();
		new File("properties/" + filename).createNewFile();
		PrintWriter out = new PrintWriter("properties/" + filename);
		for (String s : settings) {
			out.println(s);
		}
		out.close();
	}
	
	public static void load(String filename) throws IOException {
		FileInputStream fstream = new FileInputStream("properties/" + filename);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			if (strLine.startsWith("#"))
				continue;
			settings.add(strLine);
		}
		in.close();
	}
	
	public static boolean getBool(String key) {
		return getValue(key).equalsIgnoreCase("true");
	}
	
	public static int getInt(String key) {
		int toreturn = -1;
		try {
			toreturn = Integer.parseInt(getValue(key));
		} catch (Exception e) { }
		return toreturn;
	}
	
	public static String getValue(String key) {
		synchronized(settings) {
			for (String k : settings) {
				String finalk = k.split("=")[0].trim();
				if (finalk.equalsIgnoreCase(key))
					return k.split("=")[1].trim();
			}
			return "null";
		}
	}
	
	public static void updateSetting(String key, boolean value) {
		updateSetting(key, (value) ? "true" : "false");
	}
	
	public static void updateSetting(String key, int value) {
		updateSetting(key, "" + value);
	}
	
	public static void updateSetting(String key, String value) {
		removeSetting(key);
		addSetting(key, value);
	}
	
	public static void addSetting(String key, boolean value) {
		addSetting(key, (value) ? "true" : "false");
	}
	
	public static void addSetting(String key, int value) {
		addSetting(key, "" + value);
	}
	
	public static void addSetting(String key, String value) {
		synchronized(settings) {
			settings.add(key + " = " + value);
		}
	}
	
	public static void removeSetting(String key) {
		if (getValue(key).equals("null"))
			return;
		synchronized(settings) {
			for (int i = 0; i < settings.size(); i++) {
				if (settings.get(i).split("=")[0].trim().equalsIgnoreCase(key)) {
					settings.remove(i);
					break;
				}
			}
		}
	}
}

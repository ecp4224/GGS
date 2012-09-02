/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.server;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

import com.gamezgalaxy.GGS.API.EventSystem;
import com.gamezgalaxy.GGS.API.plugin.CommandHandler;
import com.gamezgalaxy.GGS.API.plugin.Plugin;
import com.gamezgalaxy.GGS.API.plugin.PluginHandler;
import com.gamezgalaxy.GGS.chat.ChatColor;
import com.gamezgalaxy.GGS.defaults.commands.*;
import com.gamezgalaxy.GGS.groups.Group;
import com.gamezgalaxy.GGS.iomodel.Player;
import com.gamezgalaxy.GGS.networking.IOClient;
import com.gamezgalaxy.GGS.networking.packets.PacketManager;
import com.gamezgalaxy.GGS.util.logger.LogInterface;
import com.gamezgalaxy.GGS.util.logger.Logger;
import com.gamezgalaxy.GGS.util.properties.Properties;
import com.gamezgalaxy.GGS.sql.ISQL;
import com.gamezgalaxy.GGS.sql.MySQL;
import com.gamezgalaxy.GGS.system.BanHandler;
import com.gamezgalaxy.GGS.system.heartbeat.Beat;
import com.gamezgalaxy.GGS.system.heartbeat.Heart;
import com.gamezgalaxy.GGS.system.heartbeat.MBeat;
import com.gamezgalaxy.GGS.system.heartbeat.WBeat;
import com.gamezgalaxy.GGS.world.Level;
import com.gamezgalaxy.GGS.world.LevelHandler;

public final class Server implements LogInterface {
	private boolean startPlugins;
	private PacketManager pm;
	private LevelHandler lm;
	private Logger logger;
	private CommandHandler ch;
	private Properties p;
	private PluginHandler ph;
	private ArrayList<Tick> ticks = new ArrayList<Tick>();
	private Thread tick;
	private Beat heartbeater;
	private EventSystem es;
	private String Salt;
	private ISQL sql;
	/**
	 * The players currently on the server
	 */
	public ArrayList<Player> players = new ArrayList<Player>();
	/**
	 * Weather the server is running or not
	 */
	public boolean Running;
	/**
	 * The port of the server
	 */
	public int Port;
	/**
	 * How many players are allowed on the server
	 */
	public int MaxPlayers;
	/**
	 * The name of the server
	 */
	public String Name;
	/**
	 * The name of the server that will appear on the WoM list
	 */
	public String altName;
	/**
	 * The description of the server
	 */
	public String description;
	/**
	 * WoM Flags
	 */
	public String flags;
	/**
	 * The MoTD of the server (The message the player sees when first joining the server)
	 */
	public String MOTD;
	/**
	 * The main level (The level the user first joins when the player joins the server)
	 */
	public Level MainLevel;
	/**
	 * Weather or not the server is public
	 */
	public boolean Public;
	/**
	 * The default filename for the system properties
	 */
	public final String configpath = "system.config";
	public final LevelHandler getLevelHandler() {
		return lm;
	}
	public final EventSystem getEventSystem() {
		return es;
	}
	public final PacketManager getPacketManager() {
		return pm;
	}
	public final CommandHandler getCommandHandler() {
		return ch;
	}
	public final PluginHandler getPluginHandler() {
		return ph;
	}
	public final ISQL getSQL() {
		return sql;
	}
	public final Properties getSystemProperties() {
		return p;
	}
	public Server(String Name, int Port, String MOTD) {
		this.Port = Port;
		this.Name = Name;
		this.MOTD = MOTD;
		tick = new Ticker();
	}
	
	/**
	 * Get the salt.
	 * <b>This method can only be called by heartbeaters and the Connect Packet.
	 * If this method is called anywhere else, then a {@link IllegalAccessException} is thrown</b>
	 * @return
	 *        The server Salt
	 * @throws IllegalAccessException
	 *                               This is thrown when an attempt to call this method
	 *                               is invalid.
	 */
	public final String getSalt() throws IllegalAccessException {
		StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
		try {
			StackTraceElement e = stacks[2]; //The heartbeat class will always be the 3rd in the stacktrace if the heartbeat is being sent correctly
			Class<?> class_ = Class.forName(e.getClassName());
			Class<? extends Heart> runClass = class_.asSubclass(Heart.class);
			return Salt;
		} catch (ClassNotFoundException e1) { }
		catch (ClassCastException e2) { }
		catch (ArrayIndexOutOfBoundsException e3) { }
		try {
			if (stacks[4].getClassName().equals("com.gamezgalaxy.GGS.networking.packets.minecraft.Connect"))
				return Salt;
		}
		catch (ArrayIndexOutOfBoundsException e3) { }
		throw new IllegalAccessException("The salt can only be accessed by the heartbeaters and the Connect packet!");
	}
	
	public void loadSystemProperties() {
		Name = getSystemProperties().getValue("Server-Name");
		altName = getSystemProperties().getValue("WOM-Alternate-Name");
		MOTD = getSystemProperties().getValue("MOTD");
		Port = getSystemProperties().getInt("Port");
		MaxPlayers = getSystemProperties().getInt("Max-Players");
		Public = getSystemProperties().getBool("Public");
		description = getSystemProperties().getValue("WOM-Server-description");
		flags = getSystemProperties().getValue("WOM-Server-Flags");
		try {
			sql = (ISQL)Class.forName(getSystemProperties().getValue("SQL-Driver")).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		sql.setPrefix(getSystemProperties().getValue("SQL-table-prefix"));
		if (sql instanceof MySQL) {
			final MySQL mysql = (MySQL)sql;
			mysql.setUsername(getSystemProperties().getValue("MySQL-username"));
			mysql.setPassword(getSystemProperties().getValue("MySQL-password"));
			mysql.setDatabase(getSystemProperties().getValue("MySQL-database-name"));
		}
	}

	public void Start() {
		if (Running)
			return;
		Running = true;
		BanHandler.init();
		es = new EventSystem(this);
		Calendar cal = Calendar.getInstance();
		cal.clear(Calendar.HOUR);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		logger = new Logger("logs/" + cal.getTime() + ".txt", this);
		String filename = cal.getTime().toString().replace(" ", "-");
		String finalname = filename.split("-")[0] + "-" + filename.split("-")[1] + "-" + filename.split("-")[2];
		try {
			logger.ChangeFilePath("logs/" , finalname + ".txt");
		} catch (IOException e2) {
			System.out.println("logs/" + finalname + ".txt");
			e2.printStackTrace();
		}
		try {
			logger.Start(false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log("Starting..");
		ch = new CommandHandler(this);
		Group.Load(this);
		p = Properties.init(this);
		loadSystemProperties();
		pm = new PacketManager(this);
		pm.StartReading();
		Log("Loading main level..");
		lm = new LevelHandler(this);
		if (!new File(getSystemProperties().getValue("MainLevel")).exists()) {
			Level l = new Level((short)64, (short)64, (short)64);
			l.name = "Main";
			l.FlatGrass();
			try {
				l.Save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		MainLevel = lm.loadLevel(getSystemProperties().getValue("MainLevel"));
		lm.loadLevels();
		tick.start();
		Log("Done!");
		Log("Generating salt");
		SecureRandom sr = null;
		try {
			sr = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < 100; i++) {
			byte[] seedb = new byte[16];
			sr.nextBytes(seedb);
			Salt = new sun.misc.BASE64Encoder().encode(seedb).replace("=", "" + ((Salt != null) ? Salt.toCharArray()[0] : "A"));
			if (new Random().nextDouble() < .3)
				break;
		}
		Salt = LetterOrNumber(Salt);
		Log("SALT: " + Salt);
		Log("Setting up SQL");
		sql.Connect(this);
		final String[] commands = new String[] {
				"CREATE TABLE if not exists " + sql.getPrefix() + "_extra (name VARCHAR(20), setting TEXT, value TEXT);",
		};
		sql.ExecuteQuery(commands);
		Log("Done!");
		Log("Create heartbeat..");
		heartbeater = new Beat(this);
		heartbeater.addHeart(new MBeat());
		heartbeater.addHeart(new WBeat());
		heartbeater.start();
		Log("Done!");
		ph = new PluginHandler();
		ph.loadplugins(this);
		try {
			addCommands();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Player findPlayer(String name) {
		Player toreturn = null;
		for (int i = 0; i < players.size(); i++) {
			if (name.equals(players.get(i).username))
				return players.get(i);
			else if (players.get(i).username.indexOf(name) != -1 && toreturn == null)
				toreturn = players.get(i);
			else if (players.get(i).username.indexOf(name) != -1 && toreturn != null)
				return null;
		}
		return toreturn;
	}

	public void addCommands() throws IOException
	{
		ch.addCommand(new Afk());
		ch.addCommand(new Ban());
		ch.addCommand(new Goto());
		ch.addCommand(new Loaded());
		ch.addCommand(new Newlvl());
		ch.addCommand(new Spawn());
		ch.addCommand(new Stop());
		ch.addCommand(new Unban());
		ch.addCommand(new TP());
		ch.addCommand(new Send());
		ch.addCommand(new Maps());
		ch.addCommand(new Load());
	}
	
	private static String LetterOrNumber(String string) {
		final String works = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		String finals = "";
		boolean change = true;
		for (char c : string.toCharArray()) {
			for (char x : works.toCharArray()) {
				if (x == c) {
					change = false;
					break;
				}
			}
			if (change)
				finals += works.toCharArray()[new Random().nextInt(works.toCharArray().length)];
			else
				finals += c;
			change = true;
		}
		return finals;
	}

	public void Stop() throws InterruptedException, IOException {
		if (!Running)
			return;
		Running = false;
		Log("Stopping server...");
		for(Player p : players)
		{
			p.sendMessage("Stopping server...");
		}

		MainLevel.Save();

		tick.join();
		logger.Stop();
		heartbeater.stop();
		for(Player p : players) // Implementing this again because I want to stop everything first.
		{						// The idea is that at the begining of the "stopping" session, the player receives that the server is stopping.
								// Then after everything has stopped but the server itself, the player is kicked from the server before actually shutdown.
			p.Kick("Server shut down. Thanks for playing!"); // Kicking so we can place a message that the server was stopped.

		}
		pm.StopReading();

		System.exit(0);
	}
	
	public void Log(String log) {
		logger.Log(log);
	}

	public boolean isStartPlugins() {
		return startPlugins;
	}

	public void setStartPlugins(boolean startPlugins) {
		this.startPlugins = startPlugins;
	}

	public Logger getLogger() {
		return logger;
	}

	public  void Add(Tick t) {
		synchronized(ticks) {
			if (!ticks.contains(t))
				ticks.add(t);
		}
	}

	public void Remove(Tick t) {
		synchronized(ticks) {
			if (ticks.contains(t))
				ticks.remove(t);
		}
	}

	private class Ticker extends Thread {

		@Override
		public void run() {
			while (Running) {
				for (int i = 0; i < ticks.size(); i++) {
					Tick t = ticks.get(i);
					try {
						t.Tick();
					} catch (Exception e) {
						Log("ERROR TICKING!");
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void onLog(String message) {
		//TODO ..colors?
		System.out.println(message);
	}
	@Override
	public void onError(String message) {
		//TODO ..colors?
		System.out.println("==!ERROR!==");
		System.out.println(message);
		System.out.println("==!ERROR!==");
	}

	public Player getPlayer(String name)
	{
		return findPlayer(name);
	}
}

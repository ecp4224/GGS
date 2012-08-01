/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.groups;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.gamezgalaxy.GGS.API.EventHandler;
import com.gamezgalaxy.GGS.API.Listener;
import com.gamezgalaxy.GGS.API.player.PlayerLoginEvent;
import com.gamezgalaxy.GGS.API.plugin.Command;
import com.gamezgalaxy.GGS.iomodel.Player;
import com.gamezgalaxy.GGS.server.Server;

public class Group {
	static ArrayList<Group> groups = new ArrayList<Group>();
	static HashMap temp = new HashMap();
	private ArrayList<String> members = new ArrayList<String>();
	private ArrayList<Player> online = new ArrayList<Player>();
	private static Group defaultgroup;
	public int permissionlevel;
	public boolean isOP;
	public String name;
	public Group parent;
	public ArrayList<String> exceptions = new ArrayList<String>(); //Commands this group can use despite permission level
	public Group(String name, int permission, boolean isOP, Group parent, Server server) {
		this.name = name;
		this.permissionlevel = permission;
		this.isOP = isOP;
		this.parent = parent;
		server.getEventSystem().registerEvents(new Listen());
	}
	public Group(String name, int permission, boolean isOP, Server server) {
		this(name, permission, isOP, null, server);
	}
	public Group(String name, Group parent, Server server) {
		this(name, parent.permissionlevel, parent.isOP, parent, server);
	}
	public boolean canExecute(Command c) {
		if (c.isOpCommand() && isOP)
			return true;
		if (c.getPermissionLevel() >= permissionlevel)
			return true;
		if (exceptions.contains(c.getName()))
			return true;
		for (String shorts : c.getShortcuts())
			if (exceptions.contains(shorts)) {
				return true;
			}
		return (parent != null) ? parent.canExecute(c) : false;
	}
	public void removeMember(String name) {
		if (!members.contains(name))
			return;
		members.remove(name);
		saveMembers();
	}
	public void addMember(String name) {
		if (members.contains(name))
			return;
		members.add(name);
		saveMembers();
	}
	public void loadMembers() throws IOException {
		if (!new File("ranks").exists())
			new File("ranks").mkdir();
		if (!new File("ranks/" + name).exists())
			return;
		FileInputStream fstream = new FileInputStream("ranks/" + name);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			if (strLine.startsWith("#"))
				continue;
			members.add(strLine);
		}
		in.close();
		
	}
	public void saveMembers() {
		if (new File("ranks/" + name).exists())
			new File("ranks/" + name).delete();
		PrintWriter out = null;
		try {
			new File("ranks/" + name).createNewFile();
			out = new PrintWriter("ranks/" + name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		for (String s : members) {
			out.println(s);
		}
		out.flush();
		out.close();
	}
	private class Listen implements Listener {
		
		@EventHandler
		public void connect(PlayerLoginEvent event) {
			for (String member : members) {
				if (event.getPlayer().username.equalsIgnoreCase(member)) {
					online.add(event.getPlayer());
					return;
				}
			}
		}
	}
	
	public static Group find(String name) {
		for (Group g : groups) {
			if (g.name.equals(name))
				return g;
		}
		return null;
	}
	
	public static Group getGroup(Player p) {
		for (Group g : groups) {
			for (Player pp : g.online) {
				if (p.username.equals(pp.username))
					return g;
			}
		}
		return null;
	}
	
	public static Group getDefault() {
		return defaultgroup;
	}

	public static void Load(Server server) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse("properties/groups.xml");
			Element elm = dom.getDocumentElement();
			NodeList nl = elm.getElementsByTagName("Group");
			if (nl != null && nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element e = (Element)nl.item(i);
					try {
						groups.add(read(e, server));
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
			}
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		Set set = temp.entrySet(); 
		// Get an iterator 
		Iterator i = set.iterator(); 
		// Display elements 
		while(i.hasNext()) { 
			Map.Entry me = (Map.Entry)i.next();
			Group g = (Group) me.getKey();
			Group parent = find((String) me.getValue());
			if (parent != null)
				g.parent = parent;
		}
		temp.clear();
		if (defaultgroup == null)
			defaultgroup = groups.get(0);
	}
	
	private static Group read(Element e, Server server) {
		String name = getTextValue(e, "name");
		boolean isOp = getTextValue(e, "isop").equalsIgnoreCase("true");
		int permission = getIntValue(e, "permission");
		String[] exceptions = new String[0];
		String parent = "null";
		boolean defaultg = false;
		try {
			parent = e.getAttribute("parent");
		} catch (Exception ee) { }
		try {
			exceptions = getTextValue(e, "exceptions").split("\\:");
		} catch (Exception ee) { }
		try {
			defaultg = getTextValue(e, "default").equalsIgnoreCase("true");
		} catch (Exception ee) { }
		Group g = new Group(name, permission, isOp, server);
		if (!parent.equals("null"))
			temp.put(g, parent);
		for (String s : exceptions) {
			g.exceptions.add(s);
		}
		if (defaultg)
			defaultgroup = g;
		return g;
	}
	
	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is 'name' I will return John
	 */
	private static String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}


	/**
	 * Calls getTextValue and returns a int value
	 */
	private static int getIntValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele,tagName));
	}
}

package com.gamezgalaxy.GGS.groups;

import java.io.IOException;
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

import com.gamezgalaxy.GGS.API.plugin.Command;

public class Group {
	static ArrayList<Group> groups = new ArrayList<Group>();
	static HashMap temp = new HashMap();
	public int permissionlevel;
	public boolean isOP;
	public String name;
	public Group parent;
	public ArrayList<String> exceptions = new ArrayList<String>(); //Commands this group can use despite permission level
	public Group(String name, int permission, boolean isOP, Group parent) {
		this.name = name;
		this.permissionlevel = permission;
		this.isOP = isOP;
		this.parent = parent;
	}
	public Group(String name, int permission, boolean isOP) {
		this(name, permission, isOP, null);
	}
	public Group(String name, Group parent) {
		this(name, parent.permissionlevel, parent.isOP, parent);
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
	
	public static Group find(String name) {
		for (Group g : groups) {
			if (g.name.equals(name))
				return g;
		}
		return null;
	}

	public static void Load() {
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
						groups.add(read(e));
						System.out.println("Group " + groups.get(groups.size() - 1).name + " loaded!");
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
			if (parent != null) {
				g.parent = parent;
				System.out.println("Parent " + parent.name + " added to " + g.name);
			}
		}
		temp.clear();
	}
	
	private static Group read(Element e) {
		String name = getTextValue(e, "name");
		boolean isOp = getTextValue(e, "isop").equalsIgnoreCase("true");
		int permission = getIntValue(e, "permission");
		String[] exceptions = new String[0];
		String parent = "null";
		try {
			parent = e.getAttribute("parent");
		} catch (Exception ee) { }
		try {
			exceptions = getTextValue(e, "exceptions").split("\\:");
		} catch (Exception ee) { }
		Group g = new Group(name, permission, isOp);
		if (!parent.equals("null"))
			temp.put(g, parent);
		for (String s : exceptions) {
			g.exceptions.add(s);
		}
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

package com.gamezgalaxy.GGS.groups;

import java.util.ArrayList;

public class Group {
	static ArrayList<Group> groups = new ArrayList<Group>();
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
}

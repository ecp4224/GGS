package com.gamezgalaxy.GGS.chat;

public enum ChatColor {
	Black('&', '0'),
	Dark_Blue('&', '1'),
	Dark_Green('&','2'),
	Dark_Aqua('&','3'),
	Dark_Red('&','4'),
	Purple('&','5'),
	Orange('&','6'),
	Grey('&', '7'),
	Dark_Grey('&','8'),
	Indigo('&','9'),
	Bright_Green('&','a'),
	Aqua('&', 'b'),
	Red('&', 'c'),
	Pink('&', 'd'),
	Yellow('&', 'e'),
	White('&', 'f');
	
	private char id;
	private char type;
	ChatColor(char id, char type) { this.id = id; this.type = type; }
	
	public char getColor() {
		return type;
	}
	public char getID() {
		return id;
	}
	
	@Override
	public String toString() {
		return "" + id + type;
	}

}

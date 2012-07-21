package com.gamezgalaxy.GGS.API;

public enum Priority {
	
	Low(0),
	
	Normal(1),
	
	High(2),
	
	VeryHigh(3),
	
	System_Level(4);
	
	
	private int important;
	
	private Priority(int important) {
		this.important = important;
	}
	
	public int getImportantance(){
		return important;
	}

}

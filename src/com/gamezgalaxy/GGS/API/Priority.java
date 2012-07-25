/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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

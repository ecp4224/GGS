/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.iomodel;

public enum ClientType {
	
	Browser((byte)0),
	
	WoM((byte)1),
	
	OpenClassic((byte)2);
	
	//TODO Add more clients
	
	byte ID;
	ClientType(byte ID) { this.ID = ID; }
	
	public byte getID() {
		return ID;
	}

}

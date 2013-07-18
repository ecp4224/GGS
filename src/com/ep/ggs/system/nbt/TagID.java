/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.system.nbt;

/**
 * An enum containing the IDs of all the tags.
 */
public enum TagID {
	END((byte) 0), BYTE((byte) 1), SHORT((byte) 2), INT((byte) 3), LONG((byte) 4), 
	FLOAT((byte) 5), DOUBLE((byte) 6), BYTE_ARRAY((byte) 7), STRING((byte) 8), 
	LIST((byte) 9), COMPOUND((byte) 10), INT_ARRAY((byte) 11);
	
	private byte ID;
	
	TagID(byte ID) {
		this.ID = ID;
	}
	
	public byte getID() {
		return ID;
	}
}

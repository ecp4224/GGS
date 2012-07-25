/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.world;

public enum PlaceMode {
	BREAK((byte)0),
	PLACE((byte)1);

	private byte type;

	PlaceMode(byte type) { this.type = type; }

	public static PlaceMode getType(byte mode) {
		switch (mode) {
		case 0:
			return BREAK;
		case 1:
			return PLACE;
		default:
			return BREAK;
		}
	}
}

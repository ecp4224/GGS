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

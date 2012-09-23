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

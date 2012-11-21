package net.mcforge.networking;

public enum ClientType {
    
    Minecraft(0),
    
    WoM(2), //Even though WoM does not send any extra packets, it will have its own type.
    
    OpenClassic(1),
    
    Extend_Classic(42);
    
    byte ID;
    ClientType(byte ID) { this.ID = ID; }
    
    ClientType(int ID) { this((byte)ID); }
    
    public byte getID() {
        return ID;
    }
    
    public static ClientType parse(byte ID) {
        for (ClientType t : ClientType.values()) {
            if (t.getID() == ID)
                return t;
        }
        return ClientType.Minecraft; //Resort to Default
    }
}


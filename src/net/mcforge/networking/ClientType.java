/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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


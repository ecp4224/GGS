/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking;

public enum ClassicClientType {
    
    Minecraft(0),
    
    WoM(2), //Even though WoM does not send any extra packets, it will have its own type.
    
    XWoM(3), //Even though XWoM does not send any extra packets, it will have its own type.
    
    OpenClassic(1),
    
    Extend_Classic(42);
    
    byte ID;
    ClassicClientType(byte ID) { this.ID = ID; }
    
    ClassicClientType(int ID) { this((byte)ID); }
    
    public byte getID() {
        return ID;
    }
    
    public static ClassicClientType parse(byte ID) {
        for (ClassicClientType t : ClassicClientType.values()) {
            if (t.getID() == ID)
                return t;
        }
        return ClassicClientType.Minecraft; //Resort to Default
    }
}


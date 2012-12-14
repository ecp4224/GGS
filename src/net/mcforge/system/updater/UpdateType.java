/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.system.updater;

public enum UpdateType {

    /**
     * Automatically update this object without notifying the user.
     */
    Auto_Silent(0),
    
    /**
     * Automatically update this object after a restart without
     * notifying the user.
     */
    Auto_Silent_Restart(1),
    
    /**
     * Automatically update this object and notify the user
     */
    Auto_Notify(2),
    
    /**
     * Automatically update this object after a restart and
     * notify the user
     */
    Auto_Notify_Restart(3),
    
    /**
     * Ask the user if he wants to update this object.
     */
    Ask(4),
    
    /**
     * The end-user must manually update the object.
     */
    Manual(5);
    
    
    byte type;
    private UpdateType(int type) { this((byte)type); }
    private UpdateType(byte type) { this.type = type; }
    
    public byte getType() {
        return type;
    }
    
    public static UpdateType parse(String type) {
        if (type.equalsIgnoreCase("auto_silent"))
            return UpdateType.Auto_Silent;
        else if (type.equalsIgnoreCase("auto_silent_restart"))
            return UpdateType.Auto_Silent_Restart;
        else if (type.equalsIgnoreCase("auto_notify"))
            return UpdateType.Auto_Notify;
        else if (type.equalsIgnoreCase("auto_notify_restart"))
            return UpdateType.Auto_Notify_Restart;
        else if (type.equalsIgnoreCase("manual"))
            return UpdateType.Manual;
        else
            return UpdateType.Ask;
    }
    
    public static UpdateType parse(int type) {
        return parse((byte)type);
    }
    
    public static UpdateType parse(byte type) {
        if (type == (byte)0)
            return UpdateType.Auto_Silent;
        else if (type == (byte)1)
            return UpdateType.Auto_Silent_Restart;
        else if (type == (byte)2)
            return UpdateType.Auto_Notify;
        else if (type == (byte)3)
            return UpdateType.Auto_Notify_Restart;
        else if (type == (byte)5)
            return UpdateType.Manual;
        else
            return UpdateType.Ask;
    }
}


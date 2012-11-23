/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.player;

import net.mcforge.API.Cancelable;
import net.mcforge.API.EventList;
import net.mcforge.iomodel.Player;

public class PlayerConnectEvent extends PlayerEvent implements Cancelable {

    private static EventList events = new EventList();
    
    private boolean _canceled = false;
    
    private String kickmsg = "";
        
        private boolean autologin = false;
    
    public PlayerConnectEvent(Player who) {
        super(who);
    }

    @Override
    public EventList getEvents() {
        return events;
    }
    /**
     * Get a list of registered listeners
     * @return The list of listeners
     */
    public static EventList getEventList() {
        return events;
    }

    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancel(boolean cancel) {
        _canceled = cancel;
    }
    
    public void setKickMessage(String msg) {
        kickmsg = msg;
    }
    
    public String getKickMessage() {
        return kickmsg;
    }
        
        public boolean getAutologin() {
            return autologin;
        }
        public void setAutologin(boolean value) {
            autologin = value;
        }
}


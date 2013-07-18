/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.API.player;

import com.ep.ggs.API.Cancelable;
import com.ep.ggs.API.EventList;
import com.ep.ggs.iomodel.Player;

public class PlayerKickedEvent extends PlayerEvent implements Cancelable {

    private String reason;
    private boolean cancel;
    private static EventList events = new EventList();
    public PlayerKickedEvent(Player who, String reason) {
        super(who);
        this.reason = reason;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
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
    
    public String getReason() {
        return reason;
    }

}

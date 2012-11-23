/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.player;

import net.mcforge.API.EventList;
import net.mcforge.iomodel.Player;

public class PlayerDisconnectEvent extends PlayerEvent {
    private static EventList events = new EventList();
    
    public PlayerDisconnectEvent(Player who) {
        super(who);
    }

    @Override
    public EventList getEvents() {
        return events;
    }
    public static EventList getEventList() {
        return events;
    }
}


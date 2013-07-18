/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.API.io;

import com.ep.ggs.API.EventList;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.Packet;
import com.ep.ggs.server.Server;

public class PacketSentEvent extends IOEvent {

    private Packet _packet;
    private static EventList events = new EventList();
    public PacketSentEvent(IOClient client, Server server, Packet packet) {
        super(client, server);
        this._packet = packet;
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
    /**
     * Get the packet the server sent
     * @return The packet sent
     */
    public Packet getPacket() {
        return _packet;
    }

}


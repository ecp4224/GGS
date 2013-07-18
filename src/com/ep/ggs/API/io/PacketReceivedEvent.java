/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.API.io;

import java.io.InputStream;

import com.ep.ggs.API.Cancelable;
import com.ep.ggs.API.EventList;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.server.Server;


public class PacketReceivedEvent extends IOEvent implements Cancelable {
    private InputStream reader;
    private byte opcode;
    private boolean _cancel;
    private static EventList events = new EventList();
    public PacketReceivedEvent(IOClient client, Server server, InputStream reader, byte opcode) {
        super(client, server);
        this.reader = reader;
        this.opcode = opcode;
    }

    @Override
    public boolean isCancelled() {
        return _cancel;
    }

    @Override
    public void setCancel(boolean cancel) {
        this._cancel = cancel;
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
     * Get a DataInputStream to read the bytes being sent by the client
     * @return A DataInputStream
     */
    public InputStream getReader() {
        return reader;
    }
    /**
     * Get the op code the client sent
     * The op code is the first byte of the byte array, this usually
     * indicates what type of message it is.
     * @return The first byte or op code
     */
    public byte getOpCode() {
        return opcode;
    }

}


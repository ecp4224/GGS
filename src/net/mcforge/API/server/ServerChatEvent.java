/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.server;

import net.mcforge.API.Cancelable;
import net.mcforge.API.EventList;
import net.mcforge.system.Console;

/**
* This event is executed when the console sends a global message. The console
* must use the {@link Console#sendGlobalMessage(String)}, or execute/handle
* the event on its own. If the end user is using a launcher that does not use
* the sendGlobalMessage method and the launcher does not execute the event, then
* this event will never be called.
*/
public class ServerChatEvent extends ServerEvent implements Cancelable  {
    private static EventList events = new EventList();
    private Console console;
    private String message;
    private boolean canceled;
    
    public ServerChatEvent(Console console, String message) {
        super(console.getServer());
        this.console = console;
        this.message = message;
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
     * Get the console that sent the message
     * @return The console.
     */
    public Console getConsole() {
        return console;
    }
    
    /**
     * Get the message sent
     * @return The message.
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Whether this event is canceled or not.
     * 
     * @return boolean
     *                If true, the event has been canceled.
     *                If false, the event has not been canceled.
     */
    public boolean isCancelled() {
        return canceled;
    }

    /**
     * Cancel/Uncancel this event.
     */
    public void setCancel(boolean cancel) {
        canceled = cancel;
    }
}


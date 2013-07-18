/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.API.server;

import com.ep.ggs.API.Event;
import com.ep.ggs.API.EventList;
import com.ep.ggs.server.Server;

public abstract class ServerEvent extends Event  {
    
    private Server server;
    
    public ServerEvent(Server server) {
        this.server = server;
    }
    
    /**
     * Get the server this event took place in
     * @return
     *        The {@link Server} object
     */
    public Server getServer() {
        return server;
    }
    
    /**
     * Get a list of registered listeners
     * @return The list of listeners
     */
    public abstract EventList getEvents();

}


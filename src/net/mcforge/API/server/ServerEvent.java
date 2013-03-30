/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.server;

import net.mcforge.API.Event;
import net.mcforge.API.EventList;
import net.mcforge.server.Server;

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


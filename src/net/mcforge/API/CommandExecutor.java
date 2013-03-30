/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API;

import net.mcforge.groups.Group;
import net.mcforge.server.Server;

public interface CommandExecutor {
    
    /**
     * Send a message to this client
     * @param message
     *               The message to send
     */
    public void sendMessage(String message);
    
    /**
     * Get the server this client belongs to
     * @return
     *        The server
     */
    public Server getServer();
    
    /**
     * Get the group this client belongs to
     * @return
     *        The group
     */
    public Group getGroup();
    
    /**
     * Get the name of this client
     * @return
     *        The name
     */
    public String getName();
}


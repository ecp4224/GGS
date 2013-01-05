/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.system;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.server.ServerChatEvent;
import net.mcforge.chat.ChatColor;
import net.mcforge.chat.Messages;
import net.mcforge.server.Server;
import net.mcforge.system.updater.Updatable;

public abstract class Console implements CommandExecutor {
    private Server server;
    private Messages m;
    
    /**
     * Set the server this console is controlling.
     * The console object might not use {@link Console#getServer()} to
     * get the server object, it may keep track of its own server object.
     * Please refer to that object's documentation. =
     * @param server
     *             The server this console is controlling.
     */
    public void setServer(Server server) {
        this.server = server;
        m = new Messages(server);
    }
    
    /**
     * Have the console send a global message
     * @param message
     *              The message for the console to send
     */
    public void sendGlobalMessage(String message) {
        ServerChatEvent event = new ServerChatEvent(this, message);
        server.getEventSystem().callEvent(event);
        if (event.isCancelled())
            return;
        m.serverBroadcast(ChatColor.Purple + "[Server] " + ChatColor.White + message);
    }
    
    /**
     * Get the server this console is controlling.
     * @return
     *        The {@link Server} object
     */
    public Server getServer() {
        return server;
    }
    
    /**
     * Get a {@link String} from the console.
     * The way the string is gotten varies from console to
     * console.
     * @return
     *        The string
     */
    public abstract String next();
    
    /**
     * This method is called when the {@link UpdateService} detects an update </br>
     * and needs to ask the console Whether to update or not. </br>
     * This method should block until an answer is given/found </br>
     * and return whether the {@link UpdateService} should update or not.
     * @param u The updatable object being updated.
     * @return
     *        Whether the {@link UpdateService} should update or not.
     */
    public abstract boolean askForUpdate(Updatable u);
    
    /**
     * This method is called when the {@link UpdateService} detects an update </br>
     * and the end user must manually download the update. </br>
     * @param u
     *         The updatable object the user must download and install.
     */
    public abstract void alertOfManualUpdate(Updatable u);
    
    /**
     * Get a {@link Integer} from the console.
     * This method uses the {@link Console#next()} method and parse
     * the input to an integer.
     * @return
     *        The int
     */
    public int nextInt() {
        String s = next();
        return Integer.parseInt(s);
    }
    
    /**
     * Get a {@link Long} from the console.
     * This method uses the {@link Console#next()} method and parse
     * the input to a long.
     * @return
     *        The long
     */
    public long nextLong() {
        String s = next();
        return Long.parseLong(s);
    }
    
    /**
     * Get a boolean from the console.
     * (Values that return true: "yes", "true", "accept", "allow", "permit")
     * This method uses the {@link Console#next()} method and parse
     * the input to an boolean.
     * @return
     *        The boolean
     */
    public boolean nextBoolean() {
        String s = next();
        return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("accept") || s.equalsIgnoreCase("allow") || s.equalsIgnoreCase("permit");
    }
}


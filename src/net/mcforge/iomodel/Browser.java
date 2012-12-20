/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.iomodel;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

import net.mcforge.API.browser.BrowserGETRequestEvent;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class Browser extends IOClient {

    public Browser(Socket client, PacketManager pm) {
        super(client, pm);
    }
    
    public PrintStream getWriter() {
        if (writer instanceof PrintStream)
            return (PrintStream)writer;
        return null;
    }
    
    /**
     * Handle a GET request. 
     * Once the GET request is handled, the client is then disconnected
     * @param request
     * @param full
     * @param server
     */
    @SuppressWarnings("deprecation")
    public void GET(String request, String full, Server server) {
        BrowserGETRequestEvent bgre = new BrowserGETRequestEvent(this, request, full);
        server.getEventSystem().callEvent(bgre);
        if (bgre.isCancelled())
            return;
        String respond = bgre.getResponse();
        if (respond.equals("")) 
            getWriter().println("HTTP/1.1 404 Not Found");
        else {
            getWriter().println("HTTP/1.1 200 OK");
            getWriter().println("Date: " + new Date().toLocaleString());
            getWriter().println("Content-Type: " + (bgre.isHTML() ? "text/html" : "text/plain"));
            getWriter().println("Content-Lenght: " + respond.getBytes().length);
            getWriter().println();
            getWriter().println(respond);
        }
        closeConnection();
    }

}


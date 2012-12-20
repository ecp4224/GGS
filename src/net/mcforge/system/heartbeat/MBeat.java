/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.system.heartbeat;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

import net.mcforge.server.Server;

public class MBeat extends Heart {
    private boolean show = false;
    @Override
    public String Prepare(Server server) {
        try {
            return "port=" + server.Port +
                    "&max=" + server.MaxPlayers +
                    "&name=" + server.Name.trim().replace(" ", "%20") +
                    "&public=" + server.Public +
                    "&version=7" +
                    "&salt=" + server.getSalt() +
                    "&users=" + server.getPlayers().size();
        } catch (IllegalAccessException e) {
            e.printStackTrace(server.getLoggerOutput());
            return "";
        }
    }
    
    @Override
    public String onPump(BufferedReader rdr, Server server) throws IOException {
        String url = "";
        String line = "";
        try {
            while (line != null) {
                line = rdr.readLine();
                if (line != null)
                    url = line;
            }
        } catch (Exception e) {
            e.printStackTrace(server.getLoggerOutput());
        } finally {
            rdr.close();
        }
        boolean flag = true;
        if (url.equals("</html>")) { url = "minecraft.net is down!"; flag = false; }
        if (!url.equals("")) {
            String newHash = url.substring(url.lastIndexOf('/') + 1);
            if (server.hash == null || server.hash.equals("") || !newHash.equals(server.hash)) {
                server.hash = newHash;
                show = false;
            }
        }
        if (!show) {
                    if (flag)
                    {
                        FileWriter fw = new FileWriter("url.txt", false);
                        fw.write(url);
                        fw.close();
                    }
            show = true;
        }
        return url;
    }

    @Override
    public String getURL() {
        return "https://minecraft.net/heartbeat.jsp";
    }
}


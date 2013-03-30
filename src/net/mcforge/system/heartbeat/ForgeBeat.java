/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.system.heartbeat;

import net.mcforge.server.Server;
import net.mcforge.world.Level;

public class ForgeBeat extends Heart {
    
    @Override
    public String Prepare(Server server) {
        return "name=" + server.Name.trim().replace(" ", "%20") +
                "&users=" + server.getClassicPlayers().size() +
                "&max=" + server.MaxPlayers +
                "&port=" + server.Port +
                "&version=" + Server.CORE_VERSION + 
                "&gcname=[Disabled]" +
                "&public=" + (server.Public ? "1" : "0") +
                "&motd=" + server.MOTD.trim().replace(" ", "%20") +
                "&worlds=" + getLevels(server) +
                "&hash=" + server.hash;
    }

    @Override
    public String getURL() {
        return "http://server.mcforge.net/hbannounce.php";
    }
    
    public String getLevels(Server server) {
        String worlds = "";
        for (Level l : server.getClassicLevelHandler().getLevelList()) {
            worlds += ", " + l.getName();
        }
        return worlds;
    }
}


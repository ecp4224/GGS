/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.system.heartbeat;

import java.io.BufferedReader;
import java.io.IOException;

import net.mcforge.server.Server;

public class WBeat extends MBeat {
    
    @Override
    public String getURL() {
        return "http://direct.worldofminecraft.com/hb.php";
    }
    
    @Override
    public String onPump(BufferedReader rdr, Server server) throws IOException {
        return ""; //Dont do anything
    }
}


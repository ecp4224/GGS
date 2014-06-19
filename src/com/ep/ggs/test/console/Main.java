/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.test.console;

import java.io.IOException;
import java.util.Scanner;

import com.ep.ggs.API.EventHandler;
import com.ep.ggs.API.Listener;
import com.ep.ggs.API.io.ServerLogEvent;
import com.ep.ggs.groups.Group;
import com.ep.ggs.server.Server;
import com.ep.ggs.server.ServerStartupArgs;
import com.ep.ggs.system.Console;
import com.ep.ggs.system.updater.Updatable;


public class Main extends Console implements Listener {
    Server s;
    static ServerStartupArgs ss = new ServerStartupArgs();
    final Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {
        for (String s : args) {
            if (s.equalsIgnoreCase("--smponly"))
                ss.setAllowClassic(false);
            if (s.equalsIgnoreCase("--classiconly"))
                ss.setAllowSMP(false);
        }
        new Main().start();
    }

    @Override
    public void sendMessage(String message) {
        s.log(message);
    }
    
    public void start() {
        s = new Server("[MCForge] Default", 25565, "Welcome!");
        s.start(this, true, ss);
        Scanner scanner = new Scanner(System.in);
        while (s.Running) {
            String line = scanner.nextLine();
            if (line.equals("/stop")) {
                try {
                    s.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(s.getLoggerOutput());
                } catch (IOException e) {
                    e.printStackTrace(s.getLoggerOutput());
                }
            }
            else if (line.startsWith("/")) {
                line = line.substring(1); //Get rid of the / at the beginning
                if (line.split("\\ ").length > 1)
                    s.getCommandHandler().execute(this, line.split("\\ ")[0], line.substring(line.indexOf(line.split("\\ ")[1])));
                else
                    s.getCommandHandler().execute(this, line, "");
            }
            else {
                super.sendGlobalMessage(line);
                s.log("[Server] " + line);
            }
        }
        System.out.println("Server stopped..");
        scanner.close();
        System.exit(0);
    }

    @Override
    public Server getServer() {
        return s;
    }
    
    @Override
    public void setServer(Server server) {
        this.s = server;
    }

    @Override
    public Group getGroup() {
        return Group.getGroupList().get(Group.getGroupList().size() - 1);
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public String next() {
        return scan.nextLine();
    }
    
    @EventHandler
    public void onLog(ServerLogEvent event) { }

    @Override
    public boolean askForUpdate(Updatable u) {
        sendMessage("An update for " + u.getDownloadPath() + " is ready for download.");
        sendMessage("Would you like to update?");
        return nextBoolean();
    }

    @Override
    public void alertOfManualUpdate(Updatable u) {
        sendMessage("An update for " + u.getName() + " is available.");
        sendMessage("You must manually download and install this update, go to " + u.getWebsite() + " to download this update.");
    }
}


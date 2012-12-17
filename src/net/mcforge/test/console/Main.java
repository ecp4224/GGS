/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.test.console;

import java.io.IOException;
import java.util.Scanner;

import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.io.ServerLogEvent;
import net.mcforge.groups.Group;
import net.mcforge.server.Server;
import net.mcforge.system.Console;
import net.mcforge.system.updater.Updatable;

public class Main extends Console implements Listener {
    Server s;
    final Scanner scan = new Scanner(System.in);
    public static void main(String[] args) {
        new Main().start();
    }

    @Override
    public void sendMessage(String message) {
        s.Log(message);
    }
    
    public void start() {
        s = new Server("[MCForge] Default", 25565, "Welcome!");
        s.Start((Console)this, true);
        Scanner scanner = new Scanner(System.in);
        while (s.Running) {
            String line = scanner.nextLine();
            if (line.equals("/stop")) {
                try {
                    s.Stop();
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
                s.Log("[Server] " + line);
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
    public Group getGroup() {
        return Group.getGroupList().get(Group.getGroupList().size() - 1);
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public String next() {
        return scan.next();
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
        sendMessage("You must manually download and install this update, go to " + u.getDownloadURL() + " to download this update.");
    }
}


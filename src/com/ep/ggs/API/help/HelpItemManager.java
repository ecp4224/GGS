/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.API.help;

import java.util.ArrayList;

import com.ep.ggs.API.EventHandler;
import com.ep.ggs.API.Listener;
import com.ep.ggs.API.plugin.Command;
import com.ep.ggs.API.plugin.CommandLoadEvent;
import com.ep.ggs.groups.Group;
import com.ep.ggs.server.Server;


public class HelpItemManager implements Listener {
    private ArrayList<HelpItem> helpitems = new ArrayList<HelpItem>();
    
    Server s;
    
    public void init(Server s) {
        this.s = s;
        s.getEventSystem().registerEvents(this);
    }
    
    public void deinit() {
        CommandLoadEvent.getEventList().unregister(this);
        helpitems.clear();
    }
    
    public boolean helpTopicExists(String type) {
        for (HelpItem h : helpitems) {
            if (h.getType().equals(type))
                return true;
        }
        return false;
    }
    
    public Command[] getCommands(String type, Group g) {
        ArrayList<Command> commands = new ArrayList<Command>();
        for (HelpItem h : helpitems) {
            if (h.getType().equals(type)) {
                if (h instanceof Command) {
                    Command c = (Command)h;
                    if (g.canExecute(c))
                        commands.add(c);
                }
            }
        }
        return commands.toArray(new Command[commands.size()]);
    }
    
    public String[] getHelpTypes() {
        ArrayList<String> types = new ArrayList<String>();
        for (HelpItem h : helpitems) {
            if (types.contains(h.getType()))
                continue;
            types.add(h.getType());
        }
        return types.toArray(new String[types.size()]);
    }
    
    @EventHandler
    public void onCommandLoad(CommandLoadEvent event) {
         Object obj = event.getCommand();
         if (obj instanceof HelpItem) {
             helpitems.add((HelpItem)obj);
             return;
         }
    }
}

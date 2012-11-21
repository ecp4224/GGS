/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.defaults.commands;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.iomodel.Player;

public class Me extends PlayerCommand
{
    @Override
    public String[] getShortcuts()
    {
        return new String[0];
    }

    @Override
    public String getName()
    {
        return "test";
    }

    @Override
    public boolean isOpCommandDefault()
    {
        return false;
    }

    @Override
    public int getDefaultPermissionLevel()
    {
        return 0;
    }

    @Override
    public void execute(Player player, String[] args)
    {
        if (args[0] == "")
        {
            player.sendMessage("/me <message> - Sends <message> under " + player.username);
        }
        else
        {
            String message = "";
            for (String s : args) {
                message += " " + s;
            }
            message = message.trim();
            for (Player p : player.getServer().players)
                p.sendMessage("You are now ranked Builder, type /help for your new set of commands. Because you get these awesome commands and I mean really awesome! BY now your most likely guessed that this is a test to guess line wrapping. IS IT WORKING, IS IT WORKING?!?!?!?!");
        }
    }

    @Override
    public void help(CommandExecutor executor) {
        // TODO Auto-generated method stub

    }
}


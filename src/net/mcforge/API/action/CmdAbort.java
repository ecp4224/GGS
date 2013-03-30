/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.action;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.plugin.PlayerCommand;
import net.mcforge.chat.ChatColor;
import net.mcforge.iomodel.Player;

public class CmdAbort extends PlayerCommand {

    @Override
    public void execute(Player player, String[] args) {
        if (!Action.hasPendingAction(player))
            player.sendMessage("You currently have no pending actions to abort.");
        Action.abortPendingActions(player);
        player.sendMessage("All your actions have been aborted.");
    }

    @Override
    public String[] getShortcuts() {
        return new String[] { "a" };
    }

    @Override
    public String getName() {
        return "abort";
    }

    @Override
    public boolean isOpCommandDefault() {
        return false;
    }

    @Override
    public int getDefaultPermissionLevel() {
        return 0;
    }

    @Override
    public void help(CommandExecutor executor) {
        executor.sendMessage("/abort - Cancels all pending actions.");
        executor.sendMessage(ChatColor.Dark_Red + "*NOTE* - This will NOT abort any current commands being executed.");
    }

}

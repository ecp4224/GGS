/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.API.action;

import com.ep.ggs.API.EventHandler;
import com.ep.ggs.API.Listener;
import com.ep.ggs.API.player.PlayerChatEvent;
import com.ep.ggs.iomodel.Player;

public class ChatAction extends Action<ChatAction> implements Listener {

    private String message;
    
    private boolean found;
    
    private ChatAction(Player p, String message) {
        setPlayer(p);
        this.message = message;
    }
    
    public ChatAction() {
    }

    /**
     * Get the message the player responded with.
     * @return
     *        The message.
     */
    public String getMessage() {
        return message;
    }
    @Override
    protected void setup() {
        getPlayer().getServer().getEventSystem().registerEvents(this);
    }

    @Override
    protected ChatAction getResponse() {
        ChatAction c = new ChatAction(getPlayer(), message);
        PlayerChatEvent.getEventList().unregister(this);
        return c;
    }

    @Override
    public boolean isCompleted() {
        return found;
    }
    
    @EventHandler
    public void onChat(PlayerChatEvent event) {
        if (event.getPlayer() == getPlayer()) {
            found = true;
            message = event.getMessage();
            super.wakeUp();
            event.setCancel(true);
        }
    }

}


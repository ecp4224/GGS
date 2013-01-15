/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.player;

import net.mcforge.API.CommandExecutor;
import net.mcforge.API.EventList;
import net.mcforge.iomodel.Player;

/**
* This event is for ban plugins that will handle ban requests from the core.
* If no plugin is handling bans, then a player will not be banned
*/
public class PlayerBanRequestEvent extends PlayerEvent {

    private CommandExecutor banner;
    private String reason;
    private boolean kick;
    private boolean banip;
    private static EventList events = new EventList();
    
    public PlayerBanRequestEvent(Player who, String reason, boolean kick, CommandExecutor banner2, boolean banip) {
        super(who);
        this.banner = banner2;
        this.reason = reason;
        this.kick = kick;
        this.banip = banip;
    }

    @Override
    public EventList getEvents() {
        return events;
    }
    
    /**
     * Whether the request is a ip ban.
     * @return
     *        True if the requst is a IP ban.
     *        False if its not.
     */
    public boolean requestIsIPBan() {
        return banip;
    }
    
    /**
     * The person that banned the player.
     * @return
     *        The banner
     */
    public CommandExecutor getBanner() {
        return banner;
    }
    
    /**
     * Get a list of registered listeners
     * @return The list of listeners
     */
    public static EventList getEventList() {
        return events;
    }
    
    /**
     * The reason the banner provided for kicking this
     * player
     * @return
     *        The reason
     */
    public String getReason() {
        return reason;
    }
    
    /**
     * Whether or not the player will be kicked
     * after the request.
     * @return
     *        True if the player will be kicked.
     *        False if the player will wont be kicked.
     */
    public boolean playerKicked() {
        return kick;
    }
}


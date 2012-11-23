/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.level;

import net.mcforge.API.Cancelable;
import net.mcforge.API.Event;
import net.mcforge.API.EventList;

public class BlockConvertEvent extends Event implements Cancelable {

    private static EventList events = new EventList();
    private byte block;
    private byte newblock;
    private boolean cancel;
    
    public BlockConvertEvent(byte block) { this.block = block; this.newblock = block; }
    
    @Override
    public EventList getEvents() {
        return events;
    }
    
    /**
     * Get a list of registered listeners
     * @return The list of listeners
     */
    public static EventList getEventList() {
        return events;
    }
    
    /**
     * Get the block that will be converted
     * @return The block
     */
    public byte getBlock() {
        return block;
    }
    
    /**
     * Get the block that {@link BlockConvertEvent#getBlock()} will be converted
     * into
     * @return The new block
     */
    public byte getNewBlock() {
        return newblock;
    }
    
    /**
     * Set the new block that {@link BlockConvertEvent#getBlock()} will be converted
     * into
     * @param newblock The new block
     * @return
     */
    public void setNewBlock(byte newblock) {
        this.newblock = newblock;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }
}

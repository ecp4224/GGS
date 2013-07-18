/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.API.player;

import com.ep.ggs.API.Cancelable;
import com.ep.ggs.API.EventList;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.server.Server;
import com.ep.ggs.world.Level;
import com.ep.ggs.world.PlaceMode;
import com.ep.ggs.world.blocks.classicmodel.ClassicBlock;

public class PlayerBlockChangeEvent extends PlayerEvent implements Cancelable {

    private static EventList events = new EventList();
    
    private boolean _canceled;
    
    private PlaceMode _type;
    
    private ClassicBlock block;
    
    private short X;
    private short Y;
    private short Z;
    private Level level;
    private Server server;
    
    
    public PlayerBlockChangeEvent(Player who, short X, short Y, short Z, ClassicBlock id, Level level, Server server, PlaceMode place) {
        super(who);
        this.block = id;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.level = level;
        this.server = server;
        this._type = place;
    }
    
    public short getX() {
        return X;
    }
    
    public short getY() {
        return Y;
    }
    
    public short getZ() {
        return Z;
    }
    
    public Level getLevel() {
        return level;
    }
    
    public Server getServer() {
        return server;
    }
    
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
    
    public ClassicBlock getBlock() {
        return block;
    }
    
    public PlaceMode getPlaceType() {
        return _type;
    }
    
    public void setBlock(ClassicBlock block) {
        this.block = block;
    }
    
    @Override
    public boolean isCancelled() {
        return _canceled;
    }

    @Override
    public void setCancel(boolean cancel) {
        _canceled = cancel;
    }
}


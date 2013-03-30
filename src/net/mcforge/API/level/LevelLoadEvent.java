/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.level;

import net.mcforge.API.Cancelable;
import net.mcforge.API.EventList;
import net.mcforge.world.Level;

public class LevelLoadEvent extends LevelEvent implements Cancelable {

    private static EventList events = new EventList();
    
    private boolean canceled;
    
    private String canceler;
    
    public LevelLoadEvent(Level level) {
        super(level);
    }

    @Override
    public EventList getEvents() {
        return events;
    }
    
    /**
     * Get the class that canceled the event
     * @return The class name
     */
    public String getCanceler() {
        return canceler;
    }
    
    /**
     * Get a list of registered listeners
     * @return The list of listeners
     */
    public static EventList getEventList() {
        return events;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancel(boolean cancel) {
        this.canceled = cancel;
        //Stacktrace:
        //0 - getStackTrace
        //1 - setCancel
        //2 - Canceler
        // ....
        this.canceler = Thread.currentThread().getStackTrace()[2].getClassName();
    }

}


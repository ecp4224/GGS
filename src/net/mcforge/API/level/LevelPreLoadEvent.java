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
import net.mcforge.world.ClassicLevel;

public class LevelPreLoadEvent extends Event implements Cancelable {

    private static EventList events = new EventList();
    
    private String filename;
    
    private ClassicLevel replace;

    private boolean cancel;
    
    public LevelPreLoadEvent(String file) {
        this.filename = file;
    }
    
    /**
     * Set the ClassicLevel to be loaded
     * In order for this ClassicLevel to be used, you must cancel the event.
     * @param ClassicLevel The ClassicLevel to load
     */
    public void setClassicLevel(ClassicLevel ClassicLevel) {
        this.replace = ClassicLevel;
    }
    
    /**
     * Get the replacement ClassicLevel that will be loaded.
     * If no replacement is set, then it will return null
     * @return The replacement ClassicLevel to load
     */
    public ClassicLevel getReplacement() {
        return replace;
    }
    
    /**
     * Get a list of registered listeners
     * @return The list of listeners
     */
    public static EventList getEventList() {
        return events;
    }
    
    /**
     * Get the file thats being loaded
     * @return
     *        The filepath
     */
    public String getFile() {
        return filename;
    }
    
    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public EventList getEvents() {
        return events;
    }

}


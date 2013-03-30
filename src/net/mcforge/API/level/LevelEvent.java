/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.level;

import net.mcforge.API.Event;
import net.mcforge.world.Level;

public abstract class LevelEvent extends Event {
    
    private Level level;
    
    public LevelEvent(Level level) {
        this.level = level;
    }
    
    /**
     * Get the level this event took place on
     * @return The level
     */
    public Level getLevel() {
        return level;
    }

}


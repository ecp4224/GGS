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


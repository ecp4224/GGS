package net.mcforge.API.level;

import net.mcforge.API.Cancelable;
import net.mcforge.API.Event;
import net.mcforge.API.EventList;
import net.mcforge.world.Level;

public class LevelPreLoadEvent extends Event implements Cancelable {

    private static EventList events = new EventList();
    
    private String filename;
    
    private Level replace;

    private boolean cancel;
    
    public LevelPreLoadEvent(String file) {
        this.filename = file;
    }
    
    /**
     * Set the level to be loaded
     * In order for this level to be used, you must cancel the event.
     * @param level The level to load
     */
    public void setLevel(Level level) {
        this.replace = level;
    }
    
    /**
     * Get the replacement level that will be loaded.
     * If no replacement is set, then it will return null
     * @return The replacement level to load
     */
    public Level getReplacement() {
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


package net.mcforge.API.player;

import net.mcforge.API.Cancelable;
import net.mcforge.API.EventList;
import net.mcforge.iomodel.Player;

public class PlayerKickedEvent extends PlayerEvent implements Cancelable {

    private String reason;
    private boolean cancel;
    private static EventList events = new EventList();
    public PlayerKickedEvent(Player who, String reason) {
        super(who);
        this.reason = reason;
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
    
    /**
     * Get a list of registered listeners
     * @return The list of listeners
     */
    public static EventList getEventList() {
        return events;
    }
    
    public String getReason() {
        return reason;
    }

}

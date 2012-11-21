package net.mcforge.API.plugin;

import net.mcforge.API.Event;
import net.mcforge.API.EventList;
import net.mcforge.server.Server;

public class PluginLoadEvent extends Event {
    
    private Plugin plugin;
    
    private Server server;
    
    private static EventList events = new EventList();
    
    public PluginLoadEvent(Plugin plugin, Server server) {
        this.server = server;
        this.plugin = plugin;
    }

    @Override
    public EventList getEvents() {
        return events;
    }
    
    /**
     * Get the server this event took place in
     * @return
     *        The {@link Server} object
     */
    public Server getServer() {
        return server;
    }
    
    /**
     * Get a list of registered listeners
     * @return The list of listeners
     */
    public static EventList getEventList() {
        return events;
    }
    
    /**
     * Get the plugin that was loaded
     * @return
     *        The {@link Plugin} object
     */
    public Plugin getPlugin() {
        return plugin;
    }

}


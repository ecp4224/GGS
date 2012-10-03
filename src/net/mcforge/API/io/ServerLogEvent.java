package net.mcforge.API.io;

import net.mcforge.API.Event;
import net.mcforge.API.EventList;
import net.mcforge.server.Server;

public class ServerLogEvent extends Event {
	
	private Server server;
	
	private String message;
	
	private String raw;
	
	private static EventList events = new EventList();
	
	public ServerLogEvent(Server server, String message, String full) {
		this.server = server;
	}
	
	/**
	 * The server that logged the event
	 * @return
	 *        The server
	 */
	public Server getServer() {
		return server;
	}
	
	/**
	 * Get the message that was written to the console.
	 * This includes the data and time appended at the start.
	 * @return
	 *        The message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Get the raw message that was written to the console.
	 * This does <b>not</b> include the date and time appended at the beginning.
	 * If you want the date and time, use {@link ServerLogEvent#getMessage()}
	 * @return
	 */
	public String getRawMessage() {
		return raw;
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

}

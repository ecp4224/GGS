package net.mcforge.API.io;

import net.mcforge.API.EventList;
import net.mcforge.API.server.ServerEvent;
import net.mcforge.server.Server;

public class ServerLogEvent extends ServerEvent {
	
	private String message;
	
	private String raw;
	
	private static EventList events = new EventList();
	
	public ServerLogEvent(Server server, String message, String full) {
		super(server);
		this.message = full;
		this.raw = message;
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

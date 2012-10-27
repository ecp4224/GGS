package net.mcforge.API.server;

import net.mcforge.API.Cancelable;
import net.mcforge.API.EventList;
import net.mcforge.system.Console;

public class ServerChatEvent extends ServerEvent implements Cancelable  {
	private static EventList events = new EventList();
	private Console console;
	private String message;
	private boolean canceled;
	
	public ServerChatEvent(Console console, String message) {
		super(console.getServer());
		this.console = console;
		this.message = message;
	}
	
	@Override
	public EventList getEvents() {
		return events;
	}
	public static EventList getEventList() {
		return events;
	}
	public Console getConsole() {
		return console;
	}
	public String getMessage() {
		return message;
	}
	public boolean isCancelled() {
		return canceled;
	}

	public void setCancel(boolean cancel) {
		canceled = cancel;
	}
}

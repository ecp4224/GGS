package net.mcforge.API.level;

import net.mcforge.API.Cancelable;
import net.mcforge.API.EventList;
import net.mcforge.world.Level;

public class LevelUnloadEvent extends LevelEvent implements Cancelable {
	private static EventList events = new EventList();

	private boolean canceled;
	
	private String canceler;

	public LevelUnloadEvent(Level level) {
		super(level);
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
	
	/**
	 * Get the class that canceled the event
	 * @return The class name
	 */
	public String getCanceler() {
		return canceler;
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

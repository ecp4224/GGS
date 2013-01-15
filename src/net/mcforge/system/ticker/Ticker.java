package net.mcforge.system.ticker;

import java.util.ArrayList;

public class Ticker {
	private ArrayList<TickData> ticks = new ArrayList<TickData>();

	private boolean run;
	
	private final int MAX_ERRORS = 7;

	private Thread ticker;

	/**
	 * Start the ticker.
	 */
	public void startTick() {
		if (run)
			return;
		run = true;
		ticker = new Runner();
		ticker.start();
	}

	/**
	 * Have the ticker stop ticking. This method will
	 * suspend until the ticker thread is finished
	 * @return
	 *        Returns true if the ticker was stopped with no errors.
	 *        Returns false if the ticker was stopped with an {@link InterruptedException} error.
	 */
	public boolean stopTick() {
		if (!run)
			return true;
		run = false;
		try {
			ticker.join();
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	/**
	 * Get an ArrayList of ticks that are ticking.
	 * @return
	 *        An ArrayList of tick objects
	 */
	public final ArrayList<TickData> getTicks() {
		return ticks;
	}

	/**
	 * Add a Tick object to the ticker.
	 * @param t
	 *         The tick object to add
	 */
	public synchronized void addTick(Tick t) {
		TickData td = new TickData(t);
		if (!ticks.contains(td))
			ticks.add(td);
		if (t.inSeperateThread())
			new TickIt(td).start();
	}

	/**
	 * Remove a Tick object from the ticker
	 * @param t
	 *         The tick object to remove
	 */
	public synchronized void removeTick(Tick t) {
		if (ticks.contains(findTick(t)))
			ticks.remove(findTick(t));
	}
	
	private TickData findTick(Tick t) {
		for (int i = 0; i < getTicks().size(); i++) {
			if (getTicks().get(i).getTick() == t)
				return getTicks().get(i);
		}
		return null;
	}

	private class TickIt extends Thread {
		private TickData t;
		public TickIt(TickData t) { this.t = t; }

		@Override
		public void run() {
			while (true) {
				if (!getTicks().contains(t))
					break;
				t.getTick().tick();
				try {
					Thread.sleep(t.getTick().getTimeout());
				} catch (InterruptedException e) { }
			}
		}
	}

	private class Runner extends Thread {

		@Override
		public void run() {
		    ArrayList<TickData> toremove = new ArrayList<TickData>();
			while (run) {
			    for (TickData t : toremove) {
			        removeTick(t.getTick());
			    }
			    toremove.clear();
				@SuppressWarnings("unchecked")
				ArrayList<TickData> temp = (ArrayList<TickData>)getTicks().clone();
				synchronized (temp) {
					for (TickData t : temp) {
						if (t.getTick().inSeperateThread())
							continue;
						if (t.getTick().getTimeout() <= t.getTime()) {
						    try {
						        t.getTick().tick();
						        t.resetError();
						    } catch (Exception e) {
						        e.printStackTrace();
						        t.addError();
						        if (t.getErrorCount() >= MAX_ERRORS) {
						            toremove.add(t);
						            System.out.println("The system tick \"" + t.getTick().getClass().getName() + "\" has been removed because it has been causing to many errors.");
						        }
						    }
							t.time = 0;
						}
						else
							t.time++;
					}
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class TickData {
		private int time;
		private Tick tick;
		private int errorcount;
		public TickData(Tick t) { this.tick = t; time = 0; }

		public int getTime() {
			return time;
		}

		public Tick getTick() {
			return tick;
		}
		
		public void addError() {
		    errorcount++;
		}
		
		public void resetError() {
		    errorcount = 0;
		}
		
		public int getErrorCount() {
		    return errorcount;
		}
	}
}

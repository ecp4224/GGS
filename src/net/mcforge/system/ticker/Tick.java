/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.system.ticker;

public interface Tick {
    
    /**
     * This is called whenever the system ticks.
     * You should handle any unhandled exceptions in this method, if to many exceptions
     * are raised in this tick, then it will be removed from the system tick.
     */
    public void tick();
    
    /**
     * Weather or not to run this tick object
     * in a separate thread
     * @return
     *        returns true if it will run in a separate thread,
     *        otherwise it will return false.
     */
    public boolean inSeperateThread();
    
    
    /**
     * How many milliseconds each tick is separated by, where 1000 is 1 second and
     * 60000 is 1 minute.
     * @return
     *        The amount of milliseconds to wait before calling the {@link Tick#tick()} method again.
     */
    public int getTimeout();
    
    /**
     * Get the name of this tick. This will be the name will appear in exception stackTraces when an unhandled exception
     * occurs. The name of the current thread will also be renamed to this by calling {@link Thread#setName(String)}
     * @return
     */
    public String tickName();

}


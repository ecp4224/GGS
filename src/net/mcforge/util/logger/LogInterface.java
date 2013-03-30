/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.util.logger;

public interface LogInterface {
    /**
     * This method is called when a line is written
     * to the log file
     * @param message
     *               The line that was written
     */
    public void onLog(String message);
    
    /**
     * This method is called when a line is written
     * to the error log file
     * @param message
     *               The line that was written
     */
    public void onError(String message);
}


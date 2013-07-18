/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.API;

public interface Cancelable {
    
    /**
     * Check to see if the event is canceled.
     * @return Whether or not the event is canceled
     */
    public boolean isCancelled();
    
    /**
     * Cancel the event.
     * This should be used when you want to stop the server from
     * doing the default action it would normally do.
     * @param cancel If set to true, the event will be canceled. If set to false, the event won't be canceled
     */
    public void setCancel(boolean cancel);

}


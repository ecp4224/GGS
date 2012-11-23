/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.browser;

import net.mcforge.API.Event;
import net.mcforge.iomodel.Browser;

public abstract class BrowserEvent extends Event {
    private Browser _b;
    
    public BrowserEvent(Browser b) {
        this._b = b;
    }
    
    public Browser getBrowserObject() {
        return _b;
    }
}


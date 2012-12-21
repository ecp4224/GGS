/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.classicminecraft.extend;

import java.io.FileNotFoundException;

public class ExtensionNotFoundException extends FileNotFoundException {
    private static final long serialVersionUID = -241304609722346101L;
    
    public ExtensionNotFoundException() { }
    
    public ExtensionNotFoundException(String msg) {
        super(msg);
    }
}


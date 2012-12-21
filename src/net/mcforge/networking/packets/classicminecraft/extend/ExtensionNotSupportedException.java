/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.classicminecraft.extend;

import java.io.FileNotFoundException;

import net.mcforge.API.ClassicExtension;

public class ExtensionNotSupportedException extends FileNotFoundException {
    private static final long serialVersionUID = -241304609722346101L;
    
    public ExtensionNotSupportedException() { }
    
    public ExtensionNotSupportedException(String msg) {
        super(msg);
    }
    
    public ExtensionNotSupportedException(ClassicExtension c) {
        this("The extension " + c.extName() + " is not supported by the client!");
    }
    
    public ExtensionNotSupportedException(Class<?> class_) {
        this(getExtension(class_));
    }
    
    public ExtensionNotSupportedException(Object o) {
        this(o.getClass());
    }
    
    private static ClassicExtension getExtension(Class<?> class_) {
        ClassicExtension c = null;
        if ((c = class_.getAnnotation(ClassicExtension.class)) != null)
            return c;
        return null;
    }
}
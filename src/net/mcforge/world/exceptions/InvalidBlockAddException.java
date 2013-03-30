/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.exceptions;

import net.mcforge.world.blocks.classicmodel.ClassicBlock;

public class InvalidBlockAddException extends IllegalAccessException {
    private static final long serialVersionUID = 2064347592399788357L;
    
    public InvalidBlockAddException(ClassicBlock b) {
        super("The block " + b.name + " uses an ID that is already in use!");
    }
    
    public InvalidBlockAddException(ClassicBlock b, String error) {
        super("The block " + b.name + " has caused an error: \"" + error + "\"");
    }

}

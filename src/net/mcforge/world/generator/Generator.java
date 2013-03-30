/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.generator;

public interface Generator<E> {
    
    /**
     * The primary name of the generator
     * that can be specified by the player
     * when creating a new level
     */
    public String getName();
    /**
     * The other names for the level generator
     * that can be specified by the player
     * when creating a new world
     */
    public String[] getShortcuts();
    
    /**
     * Generate the object.
     * @param object
     */
    public void generate(E object);
}

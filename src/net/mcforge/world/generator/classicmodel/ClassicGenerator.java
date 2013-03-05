/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.generator.classicmodel;

import net.mcforge.world.classicmodel.ClassicLevel;
import net.mcforge.world.generator.Generator;

/**
 * A ClassicGenerator that generators a level.
 * @author MCForgeTeam
 *
 */
public interface ClassicGenerator extends Generator<ClassicLevel> {
    /**
     * Generate the structure/world
     * @param l
     *         The level where to generate
     */
    public void generate(ClassicLevel l);
    
    /**
     * Generate the structure/world
     * @param l
     *         The level where to generate
     * @param x
     *         The x position where to generate
     * @param y
     *         The y position where to generate
     * @param z
     *         The z position where to generate
     */
    public void generate(ClassicLevel l, int x, int y, int z);
}


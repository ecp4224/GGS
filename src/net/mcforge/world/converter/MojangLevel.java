/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.world.converter;

import java.io.Serializable;
import net.mcforge.world.Block;
import net.mcforge.world.Level;

/**
 *
 *
 */
public class MojangLevel implements Serializable {
    public static final long serialVersionUID = 0L;
    public int cloudColor;
    public long createTime;
    public boolean creativeMode;
    public int depth;
    public int fogColor;
    public boolean growTrees;
    public int height;
    public boolean networkMode;
    public float rotSpawn;
    public int skyColor;
    public int tickCount;
    public int unprocessed;
    public int waterLevel;
    public int width;
    public int xSpawn;
    public int ySpawn;
    public int zSpawn;
    public Object blockMap;
    public byte[] blocks;
    public String creator;
    public String name;
    public Object player;
}

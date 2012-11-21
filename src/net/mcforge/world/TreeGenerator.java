/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world;

import java.util.Random;

import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;

public abstract class TreeGenerator {
    /**
     * Generates a classic type tree
     * 
     * @param serv - The server the level is on
     * @param lvl - The level to generate for
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @param z - The z coordinate
     * @param rand - The random to use
     */
    public static void generateTree(Server serv, Level lvl, short x, short y, short z, Random rand) {
        byte height = (byte) (5 + (int) (Math.random() * ((8 - 5) + 1)));
        short top = (short) (height - (2 + (int) (Math.random() * ((4 - 2) + 1))));

        for (short yy = 0; yy < height; yy++)
            Player.GlobalBlockChange(x, (short)(y + yy), z, Block.getBlock("Wood"), lvl, serv, false);

        for (short xx = (short)-top; xx <= top; ++xx) {
            for (short yy = (short)-top; yy <= top; ++yy) {
                for (short zz = (short)-top; zz <= top; ++zz) {
                    short Dist = (short)(Math.sqrt(xx * xx + yy * yy + zz * zz));
                    if (Dist < top + 1) {
                        try {
                            if (rand.nextInt((int) (Dist)) < 2) {
                                Player.GlobalBlockChange((short) (x + xx), 
                                                            (short) (y + yy + height),
                                                            (short) (z + zz),
                                                         Block.getBlock("Leaves"), 
                                                         lvl, serv, false
                                                         );
                            }
                        } 
                        catch (Exception e) {}
                    }
                }
            }
        }
    }

    /**
     * Generates a notch type tree
     * 
     * @param serv - The server the level is on
     * @param lvl - The level to generate for
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @param z - The z coordinate
     * @param rand - The random to use
     */
    public static void generateNotchTree(Server serv, Level lvl, short x, short y, short z, Random rand) {
        byte dist, 
             height = (byte)(rand.nextInt(7 - 3) + 3), 
             top = (byte)(height - 2);
        short xx, yy, zz,
              xxx, yyy, zzz;
        for (yy = 0; yy <= height; yy++) {
            Player.GlobalBlockChange(x, (short)(y + yy), z, Block.getBlock("Wood"), lvl, serv, false);
        }

        for (yy = top; yy <= height + 1; yy++) {
            dist = yy > height - 1 ? (byte)1 : (byte)2;
            for (xx = (short)-dist; xx <= dist; xx++) {
                for (zz = (short)-dist; zz <= dist; zz++) {
                    xxx = (short)(x + xx);
                    yyy = (short)(y + yy);
                    zzz = (short)(z + zz);
                    if ((xxx == x && zzz == z && yy <= height))
                        continue;

                    if (Math.abs(xx) == dist && Math.abs(zz) == dist) {
                        if (yy > height)
                            continue;
                        if (rand.nextInt(2) == 0) {
                            Player.GlobalBlockChange(xxx, yyy, zzz, Block.getBlock("Leaves"), lvl, serv, false);
                        }
                    }
                    else {
                        Player.GlobalBlockChange(xxx, yyy, zzz, Block.getBlock("Leaves"), lvl, serv, false);
                    }
                }
            }
        }
    }
    
    /**
     * Checks if a tree is near the specified location
     * 
     * @param lvl - The level to check for
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @param z - The z coordinate
     * @param dist - The distance to check for
     */
    public static boolean checkForTree(Level lvl, short x, short z, short y, short dist) { //Untested
        Block foundTile;
        for (short xx = (short)-dist; xx <= dist; ++xx) {
            for (short yy = (short)-dist; yy <= dist; ++yy) {
                for (short zz = (short)-dist; zz <= dist; ++zz) {
                    foundTile = lvl.getTile(x + xx, y + yy, z + zz);
                    if (foundTile == Block.getBlock("Wood") || foundTile == Block.getBlock("Green")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

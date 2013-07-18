/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.world.generator;

import java.util.Random;

import com.ep.ggs.iomodel.Player;
import com.ep.ggs.server.Server;
import com.ep.ggs.world.Level;
import com.ep.ggs.world.blocks.Block;
import com.ep.ggs.world.blocks.classicmodel.ClassicBlock;


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
            Player.GlobalBlockChange(x, (short)(y + yy), z, ClassicBlock.getBlock("Wood"), lvl, serv, false);

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
                                                         ClassicBlock.getBlock("Leaves"), 
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
            Player.GlobalBlockChange(x, (short)(y + yy), z, ClassicBlock.getBlock("Wood"), lvl, serv, false);
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
                            Player.GlobalBlockChange(xxx, yyy, zzz, ClassicBlock.getBlock("Leaves"), lvl, serv, false);
                        }
                    }
                    else {
                        Player.GlobalBlockChange(xxx, yyy, zzz, ClassicBlock.getBlock("Leaves"), lvl, serv, false);
                    }
                }
            }
        }
    }

    public static void generateJungleTree(Server server, Level level, short x, short y, short z, Random rand) {
    	int height = rand.nextInt(23) + 10;
   
		int xx, yy, zz;
		growLeaves(server, level, x, (short)(y + height), z , 2, rand);
		for (int yyy = y + height - 2 - rand.nextInt(4); yyy > y + height / 2; yyy -= 2 + rand.nextInt(4)) {
			float r = (float)(rand.nextFloat() * (float)Math.PI * 2F);
			xx = x + (int)(0.5F + Math.cos(r) * 4F);
			zz = z + (int)(0.5F + Math.sin(r) * 4F);
			growLeaves(server, level, (short)xx, (short)yyy, zz, 0, rand);
			
			for (int i = 0; i < 5; ++i) {
				xx = x + (int)(1.5F + Math.cos(r) * (float)i);
				zz = z + (int)(1.5F + Math.sin(r) * (float)i);
				Player.GlobalBlockChange((short)xx, (short)(yyy - 3 + i / 2), (short)zz, ClassicBlock.getBlock("Wood"), level, server);
			}
		}
		
		for (yy = 0; yy < height; ++yy) {
			Block b = level.getTile(x, y + yy, z);
			if (b.getVisibleBlock() == 0 || b.equals(ClassicBlock.getBlock("leaves"))) {
				Player.GlobalBlockChange((short)x, (short)(y + yy), (short)z, ClassicBlock.getBlock("Wood"), level, server);
			}
			
			if (yy < height - 1) {
				b = level.getTile(x + 1, y + yy, z);
				if (b.getVisibleBlock() == 0 || b.equals(ClassicBlock.getBlock("leaves"))) {
					Player.GlobalBlockChange((short)(x + 1), (short)(y + yy), (short)z, ClassicBlock.getBlock("Wood"), level, server);
				}
				
				b = level.getTile(x + 1, y + yy, z + 1);
				if (b.getVisibleBlock() == 0 || b.equals(ClassicBlock.getBlock("leaves"))) {
					Player.GlobalBlockChange((short)(x + 1), (short)(y + yy), (short)(z + 1), ClassicBlock.getBlock("Wood"), level, server);
				}
				
				b = level.getTile(x, y + yy, z + 1);
				if (b.getVisibleBlock() == 0 || b.equals(ClassicBlock.getBlock("leaves"))) {
					Player.GlobalBlockChange((short)x, (short)(y + yy), (short)(z + 1), ClassicBlock.getBlock("Wood"), level, server);
				}
			}
		}
    }
    
    private static void growLeaves(Server server, Level level, short x, short y, int z, int l, Random rand) {
    	for (int yy = y - 2; yy <= y; ++yy) {
    		int yyy = yy - y;
    		int l1 = l + 1 - yyy;
    		for (int xx = x - l1; xx <= x + l1 + 1; ++xx) {
    			int xxx = xx - x;
    			for (int zz = z - l1; zz <= z + l1 + 1; ++zz) {
    				int zzz = zz - z;
    				if ((xxx >= 0 || zzz >= 0 || Math.pow(xxx, 2) + Math.pow(zzz, 2) <= Math.pow(l1, 2)) && (xxx <= 0 && zzz <= 0 || Math.pow(xxx, 2) + Math.pow(zzz, 2) < Math.pow(l1 + 1, 2)) && (rand.nextInt(4) != 0 || Math.pow(xxx, 2) + Math.pow(zzz, 2) <= Math.pow(l1 - 1, 2))) {
    					Player.GlobalBlockChange((short)xx, (short)yy, (short)zz, ClassicBlock.getBlock("leaves"), level, server);
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
                    if (foundTile == ClassicBlock.getBlock("Wood") || foundTile == ClassicBlock.getBlock("Green")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

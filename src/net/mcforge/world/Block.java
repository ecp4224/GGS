/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world;

import java.io.Serializable;
import java.util.ArrayList;

import net.mcforge.server.Server;
import net.mcforge.world.blocks.Air;
import net.mcforge.world.blocks.Aqua;
import net.mcforge.world.blocks.Bedrock;
import net.mcforge.world.blocks.Black;
import net.mcforge.world.blocks.Blue;
import net.mcforge.world.blocks.Bookshelf;
import net.mcforge.world.blocks.Brick;
import net.mcforge.world.blocks.BrownShroom;
import net.mcforge.world.blocks.CoalOre;
import net.mcforge.world.blocks.Cobblestone;
import net.mcforge.world.blocks.Cyan;
import net.mcforge.world.blocks.Dirt;
import net.mcforge.world.blocks.DoubleStair;
import net.mcforge.world.blocks.Glass;
import net.mcforge.world.blocks.GoldBlock;
import net.mcforge.world.blocks.GoldOre;
import net.mcforge.world.blocks.Grass;
import net.mcforge.world.blocks.Gravel;
import net.mcforge.world.blocks.Gray;
import net.mcforge.world.blocks.Green;
import net.mcforge.world.blocks.Indigo;
import net.mcforge.world.blocks.IronBlock;
import net.mcforge.world.blocks.IronOre;
import net.mcforge.world.blocks.Lava;
import net.mcforge.world.blocks.Leaves;
import net.mcforge.world.blocks.Lime;
import net.mcforge.world.blocks.Magenta;
import net.mcforge.world.blocks.MossyCobblestone;
import net.mcforge.world.blocks.Obsidian;
import net.mcforge.world.blocks.Orange;
import net.mcforge.world.blocks.Pink;
import net.mcforge.world.blocks.Purple;
import net.mcforge.world.blocks.Red;
import net.mcforge.world.blocks.RedFlower;
import net.mcforge.world.blocks.RedShroom;
import net.mcforge.world.blocks.Sand;
import net.mcforge.world.blocks.Sapling;
import net.mcforge.world.blocks.Sponge;
import net.mcforge.world.blocks.Stair;
import net.mcforge.world.blocks.StationaryLava;
import net.mcforge.world.blocks.StationaryWater;
import net.mcforge.world.blocks.Stone;
import net.mcforge.world.blocks.TNT;
import net.mcforge.world.blocks.Unknown;
import net.mcforge.world.blocks.Violet;
import net.mcforge.world.blocks.Water;
import net.mcforge.world.blocks.White;
import net.mcforge.world.blocks.Wood;
import net.mcforge.world.blocks.WoodenPlank;
import net.mcforge.world.blocks.Yellow;
import net.mcforge.world.blocks.YellowFlower;
import net.mcforge.world.exceptions.InvalidBlockAddException;

/**
 * This abstract class represents a block that can be placed in the level. </br>
 * This class can be extended to create custom blocks, if you wish to create a physics block, then try using a {@linkplain PhysicsBlock#PhysicsBlock(byte, String)} </br>
 * To add a block, you would call the static method {@link Block#addBlock(Block)}. This will allow other plugins, commands </br>
 * to find your block, be sure to use a unused ID, otherwise you will throw an {@linkplain InvalidBlockAddException#InvalidBlockAddException()}. </br>
 * @author MCForgeTeam
 *
 */
public abstract class Block implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Block[] blocks = new Block[] {
            new Air(),
            new Aqua(),
            new Bedrock(),
            new Black(),
            new Blue(),
            new Bookshelf(),
            new Brick(),
            new BrownShroom(),
            new CoalOre(),
            new Cobblestone(),
            new Cyan(),
            new Dirt(),
            new DoubleStair(),
            new Glass(),
            new GoldBlock(),
            new GoldOre(),
            new Grass(),
            new Gravel(),
            new Gray(),
            new Green(),
            new Indigo(),
            new IronBlock(),
            new IronOre(),
            new Lava(),
            new Leaves(),
            new Lime(),
            new Magenta(),
            new MossyCobblestone(),
            new Obsidian(),
            new Orange(),
            new Pink(),
            new Purple(),
            new Red(),
            new RedFlower(),
            new RedShroom(),
            new Sand(),
            new Sapling(),
            new Sponge(),
            new Stair(),
            new StationaryLava(),
            new StationaryWater(),
            new Stone(),
            new TNT(),
            new Unknown(),
            new Violet(),
            new Water(),
            new White(),
            new Wood(),
            new WoodenPlank(),
            new Yellow(),
            new YellowFlower()
    };
    
    private static ArrayList<Block> custom = new ArrayList<Block>();
    
    /**
     * The ID of the block
     */
    public byte ID;
    
    /**
     * The name of the block
     */
    public String name;
    
    /**
     * This method is called when the block is placed
     * @param l 
     *          The level the block was placed in
     * @param index 
     */
    public void onPlace(Level l, int x, int y, int z, Server server) {
        
    }
    
    /**
     * Get the block ID the client will see
     * @return 
     *        The ID
     */
    public byte getVisibleBlock() {
        return ID;
    }
    
    /**
     * This method is called when the block is deleted
     * @param l 
     *          The level the block was deleted in
     * @param index 
     */
    public void onDelete(Level l, int x, int y, int z, Server server) {
        
    }
    
    /**
     * Add a custom block to the block list
     * @param b 
     *         An empty version of the custom block.
     */
    public static void addBlock(Block b) {
        if (custom.contains(b))
            return;
        custom.add(b);
    }
    
    /**
     * Remove a custom block from the block list
     * @param b
     *         The block to remove
     */
    public static void removeBlock(Block b) {
        if (!custom.contains(b))
            return;
        custom.remove(b);
    }
    
    public boolean canWalkThrough() {
        return false;
    }
    
    /**
     * Initialize a new block
     * @param ID 
     *           The ID of the block
     * @param name 
     *            The name of the block
     */
    public Block(byte ID, String name) {
        this.ID = ID;
        this.name = name;
    }
    
    /**
     * Initialize a new block
     */
    public Block() { }
    
    /**
     * Get a block by ID
     * @param ID 
     *          The ID of the block to look for
     * @return 
     *        An empty version of the block with the basic information in it </br>
     *        If the block specified isn't found, then a new Unknown block is return </br>
     *        You can check to see if an the Unknown block was returned by doing </br>
     *        <code>getBlock(0).name.equals("NULL")</code>
     */
    public static Block getBlock(byte ID) {
        for (Block b : blocks) {
            if (b.ID == ID)
                return b;
        }
        for (Block b : custom) {
            if (b.ID == ID)
                return b;
        }
        return new Unknown();
    }
    
    /**
     * Get a block by name
     * @param name 
     *            The name of the block to look for
     * @return 
     *        An empty version of the block with the basic information in it </br>
     *        If the block specified isn't found, then a new Unknown block is return </br>
     *        You can check to see if an the Unknown block was returned by doing </br>
     *        <code>getBlock("ABlock").name.equals("NULL")</code>
     */
    public static Block getBlock(String name) {
        for (Block b : blocks) {
            if (b.name.equalsIgnoreCase(name))
                return b;
        }
        for (Block b : custom) {
            if (b.name.equalsIgnoreCase(name))
                return b;
        }
        return new Unknown();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Block) {
            Block block = (Block)obj;
            return block.name.equals(name) && block.getVisibleBlock() == getVisibleBlock();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }

}


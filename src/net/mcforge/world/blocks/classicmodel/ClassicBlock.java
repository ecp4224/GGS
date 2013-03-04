/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.blocks.classicmodel;

import java.io.Serializable;
import java.util.ArrayList;

import net.mcforge.server.Server;
import net.mcforge.world.Level;
import net.mcforge.world.blocks.Block;
import net.mcforge.world.exceptions.InvalidBlockAddException;

/**
 * This abstract class represents a block that can be placed in the level. </br>
 * This class can be extended to create custom blocks, if you wish to create a physics block, then try using a {@linkplain PhysicsBlock#PhysicsBlock(byte, String)} </br>
 * To add a block, you would call the static method {@link ClassicBlock#addBlock(ClassicBlock)}. This will allow other plugins, commands </br>
 * to find your block, be sure to use a unused ID, otherwise you will throw an {@linkplain InvalidBlockAddException#InvalidBlockAddException()}. </br>
 * @author MCForgeTeam
 *
 */
public abstract class ClassicBlock implements Serializable, Block {
    private static final long serialVersionUID = 1L;
    
    private static ClassicBlock[] blocks = new ClassicBlock[] {
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
    
    private static ArrayList<ClassicBlock> custom = new ArrayList<ClassicBlock>();
    
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
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
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
    public static void addBlock(ClassicBlock b) {
        if (custom.contains(b))
            return;
        custom.add(b);
    }
    
    /**
     * Remove a custom block from the block list
     * @param b
     *         The block to remove
     */
    public static void removeBlock(ClassicBlock b) {
        if (!custom.contains(b))
            return;
        custom.remove(b);
    }
    
    @Override
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
    public ClassicBlock(byte ID, String name) {
        this.ID = ID;
        this.name = name;
    }
    
    /**
     * Initialize a new block
     */
    public ClassicBlock() { }
    
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
    public static ClassicBlock getBlock(byte ID) {
        for (ClassicBlock b : blocks) {
            if (b.ID == ID)
                return b;
        }
        for (ClassicBlock b : custom) {
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
    public static ClassicBlock getBlock(String name) {
        for (ClassicBlock b : blocks) {
            if (b.name.equalsIgnoreCase(name))
                return b;
        }
        for (ClassicBlock b : custom) {
            if (b.name.equalsIgnoreCase(name))
                return b;
        }
        return new Unknown();
    }

}


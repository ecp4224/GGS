/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.blocks;

import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;
import net.mcforge.system.ticker.Tick;
import net.mcforge.world.Level;

public abstract class PhysicsBlock extends Block implements Tick {
    private static final long serialVersionUID = 3223381644534019388L;
    
    private int _x;
    private int _y;
    private int _z;
    private Level _level;
    private transient Server _server;
    public PhysicsBlock(byte ID, String name) {
        super(ID, name);
    }
    public PhysicsBlock(byte ID, String name, Server server) {
        super(ID, name);
        this._server = server;
    }
    
    @Override
    public void onDelete(Level l, int x, int y, int z, Server server) {
        if (_level != l)
            return;
        _level.getTicks().remove(this);
        _level = null;
        _server = null;
    }
    
    /**
     * Whether this block should be added to the tick
     * when the level loads.
     * @return
     *        True if this block should be added to the tick when the level loads.
     *        False if this block should not be added to the tick when the level loads.
     */
    public abstract boolean initAtStart();
    
    /**
     * Create a clone of the Physics Block.
     * @param s
     * @return The clone of the block
     */
    public abstract PhysicsBlock clone(Server s);
    
    /**
     * Whether this physics block should tick
     * outside the physics thread.
     * @return
     *        True if it should tick outside the physics
     *        thread, otherwise false.
     */
    public boolean runInSeperateThread() {
        return false;
    }
    
    /**
     * Occurs whenever the server ticks (every 500 ms)
     */
    public abstract void tick();
    
    /**
     * Change the pos of the physics block.
     * This does NOT update the physics block
     * @param x The x cord.
     * @param y The y cord.
     * @param z The z cord.
     */
    public void setPos(int x, int y, int z) {
        this._x = x;
        this._y = y;
        this._z = z;
    }
    
    /**
     * Change the pos of the physics block
     * This does NOT update the physics block
     * @param pos
     *           The location array in x, y, z
     */
    public void setPos(int[] pos) {
        setPos(pos[0], pos[1], pos[2]);
    }
    
    /**
     * Change the level of the physics block
     * This method does NOT update the physics block
     * @param level The new level
     */
    public void setLevel(Level level) {
        this._level = level;
    }
    
    /**
     * Move the block
     * This method does update the physics block
     * @param newx The new x cord.
     * @param newy The new y cord.
     * @param newz The new z cord.
     */
    public void move(int newx, int newy, int newz) {
        if (_level == null)
            return;
        add(newx, newy, newz);
        remove();
    }
    
    /**
     * Create a new type of THIS physics block
     * @param newx The x cord. of the new block
     * @param newy The y cord. of the new block
     * @param newz The z cord. of the new block
     */
    public void add(int newx, int newy, int newz) {
        if (_level == null)
            return;
        Player.GlobalBlockChange((short)newx, (short)newy, (short)newz, this, _level, _server);
    }
    
    /**
     * Remove a physics block
     * @param x The x cord. of the block
     * @param y The y cord. of the block
     * @param z The z cord. of the block
     */
    public void remove(int x, int y, int z) {
        if (_level == null)
            return;
        Player.GlobalBlockChange((short)x, (short)y, (short)z, Block.getBlock("Air"), _level, _server);
    }
    
    /**
     * Change this block into another block.
     * @param b
     *         The block to change into.
     */
    public void change(Block b) {
        if (_level == null)
            return;
        Player.GlobalBlockChange((short)_x, (short)_y, (short)_z, b, _level, _server);
    }
    
    /**
     * Stop this block from ticking
     */
    public void stopTick() {
        if (_level == null)
            return;
        if (_level.getTicks() == null)
            return;
        if (_level.getTicks().contains(this))
            _level.getTicks().remove(this);
    }
    
    /**
     * Remove THIS physics block
     */
    public void remove() {
        remove(_x, _y, _z);
    }
    
    /**
     * Get the current X cord of the block
     * @return The X pos
     */
    public int getX() {
        return _x;
    }
    
    /**
     * Get the current Y cord of the block
     * @return The Y pos
     */
    public int getY() {
        return _y;
    }
    
    /**
     * Get the current Z cord of the block
     * @return The Z pos
     */
    public int getZ() {
        return _z;
    }
    
    /**
     * Get the current level
     * @return The level this block is in
     */
    public Level getLevel() {
        return _level;
    }
    
    /**
     * Whether a physicsblock is active. When a physicsblock
     * is inactive, it wont tick.
     * @param pb
     *          The block to check
     * @return
     *        True if the block is active, false if its not
     */
    public boolean isBlockActive(PhysicsBlock pb) {
        if (pb == null)
            return false;
        return _level.getTicks().contains(pb);
    }
    
    /**
     * Whether this block is active. If this block is
     * inactive, it wont tick
     * @return
     *        True if this block is active, false if its not
     */
    public boolean isBlockActive() {
        return isBlockActive(this);
    }
    
    /**
     * Get the server this block belongs
     * to
     * @return
     *        The server object
     */
    public Server getServer() {
        return _server;
    }
    
    public void setServer(Server server) {
        this._server = server;
    }

}


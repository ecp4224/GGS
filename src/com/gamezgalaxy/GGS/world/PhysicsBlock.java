package com.gamezgalaxy.GGS.world;

import com.gamezgalaxy.GGS.server.Player;
import com.gamezgalaxy.GGS.server.Server;
import com.gamezgalaxy.GGS.server.Tick;

public abstract class PhysicsBlock extends Block implements Tick {

	private int _x;
	private int _y;
	private int _z;
	private Level _level;
	private Server _server;
	public PhysicsBlock(byte ID, String name) {
		super(ID, name);
	}
	public PhysicsBlock(byte ID, String name, Server server) {
		super(ID, name);
		this._server = server;
	}
	
	/**
	 * Create a clone of the Physics Block.
	 * @param s
	 * @return
	 */
	public abstract PhysicsBlock clone(Server s);
	
	/**
	 * Occurs whenever the server ticks (every 500 ms)
	 */
	public abstract void Tick();
	
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
		_level.setTile(Block.getBlock((byte)0), _x, _y, _z, _server);
		Player.GlobalBlockChange((short)_x, (short)_y, (short)_z, Block.getBlock((byte)0), _level, _server, false);
		setPos(newx, newy, newz);
		_level.setTile(this, _x,  _y, _z, _server);
		Player.GlobalBlockChange((short)_x, (short)_y, (short)_z, this, _level, _server, false);
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
		_level.setTile(this, newx, newy, newz, _server);
		Player.GlobalBlockChange((short)_x, (short)_y, (short)_z, this, _level, _server, false);
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
		_level.setTile(Block.getBlock((byte)0), x, y, z, _server);
		Player.GlobalBlockChange((short)_x, (short)_y, (short)_z, this, _level, _server, false);
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

}

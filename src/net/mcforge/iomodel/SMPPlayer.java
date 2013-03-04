package net.mcforge.iomodel;

import java.net.Socket;
import java.util.Random;

import net.mcforge.entity.Location;
import net.mcforge.entity.Rotation;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;
import net.mcforge.system.ticker.Tick;

public class SMPPlayer extends IOClient implements Tick {
	private final static Random rand = new Random();
	private int pingID;
	private int dicpingc;
	private boolean pinged;

	public String username;

	private Location location;
	private Rotation rotation;
	private Location oldLocation;
	private Rotation oldRotation;
	private double stance;
	private boolean onGround;

	private short heldItemSlot;

	public SMPPlayer(Socket client, PacketManager pm) {
		super(client, pm);
		pm.server.getTicker().addTick(this);
	}

	public void kick(String reason) {
		Packet p = pm.getPacket("SMPKick");
		p.Write(this, pm.server, reason);
	}

	public int getPingID() {
		return pingID;
	}

	public void ping() {
		pinged = true;
	}

	/**
	 * Whether the player is on ground.<br>
	 * If the value is <b>True</b>, the player is either walking or swiming. If
	 * the value is <b>False</b> the player is jumping or falling.<br>
	 * This value is set by the
	 * {@link net.mcforge.networking.packets.minecraft.Player Player packet}.
	 */
	public boolean isOnGround() {
		return onGround;
	}

	/**
	 * Controls if the player is marked as on ground.<br>
	 * This value should only be set by the
	 * {@link net.mcforge.networking.packets.minecraft.Player Player packet}.
	 */
	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	/**
	 * Gets the player's {@link Location location}.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the player's {@link Location location}.
	 * 
	 * @param location
	 *            The new location.
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * Gets the player's {@link Rotation rotation}.
	 */
	public Rotation getRotation() {
		return rotation;
	}

	/**
	 * Sets the player's {@link Rotation rotation}.
	 */
	public void setRotation(Rotation rotation) {
		this.rotation = rotation;
	}

	/**
	 * Gets the player's location before the {@link #getLocation() current
	 * location}.
	 */
	public Location getOldLocation() {
		return oldLocation;
	}

	/**
	 * Sets the player's location before the {@link #setLocation() current
	 * location}.
	 * 
	 * @param oldlocation
	 *            The new old location.
	 */
	public void setOldLocation(Location oldlocation) {
		this.oldLocation = oldlocation;
	}

	/**
	 * Gets the player's rotation before the {@link #getRotation() current
	 * location}.
	 */
	public Rotation getOldRotation() {
		return oldRotation;
	}

	/**
	 * Gets the player's rotation before the {@link #setRotation() current
	 * location}.
	 * 
	 * @param oldrotation
	 *            The new old rotation.
	 */
	public void setOldRotation(Rotation oldrotation) {
		this.oldRotation = oldrotation;
	}

	/**
	 * Gets the player's stance.<br>
	 * The stance is used to modify the players bounding box when going up
	 * stairs, crouching, etc...
	 */
	public double getStance() {
		return stance;
	}

	/**
	 * Sets the player's stance.<br>
	 * The stance is used to modify the players bounding box when going up
	 * stairs, crouching, etc...
	 * 
	 * @param stance
	 *            The new stance.
	 */
	public void setStance(double stance) {
		this.stance = stance;
	}
	
	/**
	 * Gets the slot ID of the currently held item.<br>
	 * The slot ID ranges from 0 - 8.
	 */
	public short getHeldItemSlot() {
		return heldItemSlot;
	}

	/**
	 * Sets the slot ID of the currently held item.<br>
	 * The slot ID ranges from 0 - 8.
	 * 
	 * @param heldItemSlot
	 *            The new held item slot.
	 */
	public void setHeldItemSlot(short heldItemSlot) {
		if (heldItemSlot < 0 || heldItemSlot > 8) { 
			return; 
		}

		this.heldItemSlot = heldItemSlot;
	}

	@Override
	public void tick() {
		if (!pinged) {
			dicpingc++;
			if (dicpingc > 4) {
				closeConnection();
				return;
			}
		}
		else
			dicpingc = 0;
		pinged = false;
		pingID = rand.nextInt();
		Packet p = pm.getPacket("KeepAlive");
		p.Write(this, getServer(), pingID);
	}

	public Server getServer() {
		return pm.server;
	}

	@Override
	public void closeConnection() {
		getServer().getTicker().removeTick(this);
		super.closeConnection();
	}

	@Override
	public boolean inSeperateThread() {
		return false;
	}

	@Override
	public int getTimeout() {
		return 60000 / 4;
	}

	@Override
	public String tickName() {
		return "SMPTick";
	}

}

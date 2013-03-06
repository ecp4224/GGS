package net.mcforge.iomodel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import net.mcforge.API.CommandExecutor;
import net.mcforge.entity.Location;
import net.mcforge.entity.Rotation;
import net.mcforge.groups.Group;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;
import net.mcforge.system.ticker.Tick;
import net.mcforge.util.WebUtils;

public class SMPPlayer extends IOClient implements Tick, CommandExecutor {
	private final static Random rand = new Random();
	private int pingID;
	private int dicpingc;
	private boolean pinged;
	private KeyPair privateKey;
	private byte[] verify = new byte[4];
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
	
	public void requestLogin() {
	    try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            privateKey = kpg.generateKeyPair();
            new Random().nextBytes(verify);
            Packet p = pm.getPacket("EKR");
            if (p == null)
                return;
            p.Write(this, getServer(), privateKey.getPublic(), verify);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
	}
	
	public void validateLogin(byte[] sharedkey, byte[] verify, Packet packet) {
	    try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey.getPrivate());
            byte[] data = cipher.doFinal(sharedkey);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update("IRAIDYUS".getBytes());
            md.update(data);
            md.update(privateKey.getPublic().getEncoded());
            String hash = bytesToHex(md.digest());
            md = null;
            System.out.println(hash);
            String check = WebUtils.readContentsToArray(new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + getName() + "&serverId=" + hash))[0];
            System.out.println(check);
            if (!check.equals("YES")) {
                kick("Invalid login!");
                return;
            }
            packet.Write(this, getServer());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private static String bytesToHex(byte[] bytes) {
	    final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	    boolean negitive = (bytes[0] & 0x80) == 0x80;
	    if (negitive)
	        bytes = twosCompliment(bytes);
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return negitive ? "-" + new String(hexChars) : new String(hexChars);
	}
	
	private static byte[] twosCompliment(byte[] p) {
        int i;
        boolean carry = true;
        for (i = p.length - 1; i >= 0; i--) {
            p[i] = (byte)~p[i];
            if (carry) {
                carry = p[i] == 0xFF;
                p[i]++;
            }
        }
        return p;
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

    @Override
    public void sendMessage(String message) {
        //TODO Send a message
    }

    @Override
    public Group getGroup() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getName() {
        return username;
    }

}

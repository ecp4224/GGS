package net.mcforge.iomodel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bukkit.Achievement;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.mcforge.API.CommandExecutor;
import net.mcforge.entity.NetworkEntity;
import net.mcforge.entity.Rotation;
import net.mcforge.groups.Group;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;
import net.mcforge.util.WebUtils;

public class SMPPlayer extends NetworkEntity implements CommandExecutor, org.bukkit.entity.Player {
	private final static Random rand = new Random();
	private int pingID;
	private int dicpingc;
	private boolean pinged;
	private KeyPair privateKey;
	private byte[] verify = new byte[4];
	private byte lstep;
	public String username;
	private Cipher cipher;
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
	
	public void requestLogin() throws IllegalAccessException {
	    if (lstep != 0)
	        throw new IllegalAccessException("This method can only be invoked during login.");
	    try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            privateKey = kpg.generateKeyPair();
            new Random().nextBytes(verify);
            Packet p = pm.getPacket("EKR");
            if (p == null)
                return;
            p.Write(this, getServer(), privateKey.getPublic(), verify);
            lstep = 1;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
	}
	
	public void validateLogin(byte[] sharedkey, byte[] verify) throws IllegalAccessException {
	    if (lstep != 1)
	        throw new IllegalAccessException("This method can only be invoked during login.");
	    try {
	        getServer().Log("Something you find with a Public Key..", true);
	        if (cipher == null) {
	            cipher = Cipher.getInstance("RSA");
	            cipher.init(Cipher.DECRYPT_MODE, privateKey.getPrivate());
	        }
            byte[] data = cipher.doFinal(sharedkey);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update("IRAIDYUS".getBytes());
            md.update(data);
            md.update(privateKey.getPublic().getEncoded());
            String hash = bytesToHex(md.digest());
            md = null;
            getServer().Log("CLIENT: " + hash, true);
            String check = WebUtils.readContentsToArray(new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + getName() + "&serverId=" + hash))[0];
            getServer().Log("SURVEY SAYS \"" + check + "\"", true);
            if (!check.equals("YES")) {
                getServer().Log("I'm sorry, it seems you didn't get the answer. Well thanks for playing :)", true);
                kick("Invalid login!");
                return;
            }
            lstep = 2;
            CipherOutputStream out = new CipherOutputStream(getClient().getOutputStream(), cipher);
            setOutputStream(out);
            CipherInputStream in = new CipherInputStream(getClient().getInputStream(), cipher);
            setInputStream(in);
            login();
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
	
	public void login() throws IllegalAccessException {
	    if (lstep != 2)
	        throw new IllegalAccessException("This method can only be invoked during login.");
	    getServer().Log(getName() + " has joined the server!");
        Packet packet = pm.getPacket((byte)0x01, getClientType());
        if (packet == null)
            throw new RuntimeException("ClientStatuses packet can't be found!");
        packet.Write(this, getServer(), 0, "flat", (byte)0, (byte)0, (byte)0, (byte)0, (byte)25);
	    //TODO Stuff
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

    @Override
    public void closeInventory() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Inventory getEnderChest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getExpToLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public GameMode getGameMode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PlayerInventory getInventory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemStack getItemInHand() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ItemStack getItemOnCursor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView getOpenInventory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getSleepTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isBlocking() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSleeping() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public InventoryView openEnchanting(Location location, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InventoryView openInventory(Inventory inventory) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void openInventory(InventoryView inventory) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public InventoryView openWorkbench(Location location, boolean force) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setGameMode(GameMode mode) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setItemInHand(ItemStack item) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setItemOnCursor(ItemStack item) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean setWindowProperty(Property prop, int value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Collection<PotionEffect> getActivePotionEffects() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getCanPickupItems() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public EntityEquipment getEquipment() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public double getEyeHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getEyeHeight(boolean ignoreSneaking) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Location getEyeLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Player getKiller() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLastDamage() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaximumAir() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMaximumNoDamageTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNoDamageTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getRemainingAir() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean getRemoveWhenFarAway() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasLineOfSight(Entity other) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType type) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removePotionEffect(PotionEffectType type) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setCanPickupItems(boolean pickup) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setLastDamage(int damage) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMaximumAir(int ticks) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMaximumNoDamageTicks(int ticks) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setNoDamageTicks(int ticks) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setRemainingAir(int ticks) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setRemoveWhenFarAway(boolean remove) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Arrow shootArrow() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Egg throwEgg() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Snowball throwSnowball() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void damage(int amount) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void damage(int amount, Entity source) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getHealth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMaxHealth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void resetMaxHealth() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setHealth(int health) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMaxHealth(int health) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasPermission(String name) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPermissionSet(String name) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void recalculatePermissions() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isOp() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setOp(boolean value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void acceptConversationInput(String input) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isConversing() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void sendMessage(String[] messages) {
        for (int i = 0; i < messages.length; i++) {
        	sendMessage(messages[i]);
        }
    }

    @Override
    public long getFirstPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLastPlayed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Player getPlayer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasPlayedBefore() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBanned() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isOnline() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isWhitelisted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setBanned(boolean banned) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setWhitelisted(boolean value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Map<String, Object> serialize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void awardAchievement(Achievement achievement) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean canSee(Player player) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void chat(String msg) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public InetSocketAddress getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getAllowFlight() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Location getBedSpawnLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getCompassTarget() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float getExhaustion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getExp() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getFlySpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getFoodLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getLevel() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getPlayerListName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getPlayerTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getPlayerTimeOffset() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getSaturation() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getTotalExperience() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getWalkSpeed() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void giveExp(int amount) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void giveExpLevels(int amount) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hidePlayer(Player player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material,
            int amount) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isFlying() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPlayerTimeRelative() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSleepingIgnored() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSneaking() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSprinting() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void kickPlayer(String message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void loadData() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean performCommand(String command) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void playSound(Location location, Sound sound, float volume, float pitch) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetPlayerTime() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveData() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendBlockChange(Location loc, int material, byte data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void sendMap(MapView map) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sendRawMessage(String message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setAllowFlight(boolean flight) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean force) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setCompassTarget(Location loc) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDisplayName(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setExhaustion(float value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setExp(float exp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFlySpeed(float value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFlying(boolean value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFoodLevel(int value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setLevel(int level) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPlayerListName(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSaturation(float value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSneaking(boolean sneak) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSprinting(boolean sprinting) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setTexturePack(String url) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setTotalExperience(int exp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setWalkSpeed(float value) throws IllegalArgumentException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void showPlayer(Player player) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateInventory() {
        // TODO Auto-generated method stub
        
    }
}

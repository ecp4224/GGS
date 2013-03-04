/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.iomodel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.mcforge.API.ClassicExtension;
import net.mcforge.API.CommandExecutor;
import net.mcforge.API.level.PlayerJoinedLevel;
import net.mcforge.API.player.PlayerBanRequestEvent;
import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.API.player.PlayerChatEvent;
import net.mcforge.API.player.PlayerCommandEvent;
import net.mcforge.API.player.PlayerDisconnectEvent;
import net.mcforge.API.player.PlayerKickedEvent;
import net.mcforge.chat.ChatColor;
import net.mcforge.chat.Messages;
import net.mcforge.groups.Group;
import net.mcforge.networking.ClassicClientType;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.classicminecraft.GlobalPosUpdate;
import net.mcforge.networking.packets.classicminecraft.SetBlock;
import net.mcforge.networking.packets.classicminecraft.TP;
import net.mcforge.server.Server;
import net.mcforge.sql.MySQL;
import net.mcforge.system.ticker.Tick;
import net.mcforge.world.Level;
import net.mcforge.world.PlaceMode;
import net.mcforge.world.blocks.BlockUpdate;
import net.mcforge.world.blocks.classicmodel.ClassicBlock;

public class Player extends IOClient implements CommandExecutor, Tick {
    protected short X;
    protected short Y;
    protected short Z;
    protected byte ID;
    protected byte block;
    protected boolean showprefix;
    protected String prefix;
    protected ArrayList<ClassicExtension> extend = new ArrayList<ClassicExtension>();
    protected Level level;
    protected int money;
    protected Thread levelsender;
    protected Messages chat;
    protected String clientName = "Minecraft";
    protected ArrayList<Player> seeable = new ArrayList<Player>();
    protected ChatColor color = ChatColor.White;
    protected String custom_name;
    protected String appended_message = "";
    private HashMap<String, Object> extra = new HashMap<String, Object>();
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static MessageDigest digest;
    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }
    /**
     * Whether or not the player is logged in
     */
    public boolean isLoggedin;
    /**
     * The reason why the player was kicked
     */
    public String kickreason;
    /**
     * The username of the player
     */
    public String username;
    /**
     * The world the player is currently in
     */
    public String world;
    /**
     * The mppass the user used to login
     */
    public String mppass;
    /**
     * The message the player last send
     */
    public String message;
    /**
     * Whether or not the player is connected
     */
    public boolean isConnected;
    /**
     * Whether or not the player can use color codes
     */
    public boolean cc = true; //Can Player use color codes
    /**
     * What type of client the player is using.
     */
    public ClassicClientType client;
    /**
     * The last X pos of the player
     */
    public short oldX;
    /**
     * The last Y pos of the player
     */
    public short oldY;
    /**
     * The last Z pos of the player
     */
    public short oldZ;
    /**
     * The current yaw of the player
     */
    public byte yaw;
    /**
     * The current pitch of the player
     */
    public byte pitch;
    /**
     * The old yaw of the player
     */
    public byte oldyaw;
    /**
     * The old pitch of the player
     */
    public byte oldpitch;
    /**
     * The activity of the player.
     */
    private boolean afk;
    /**
     * This is the last byte in the Connect Packet
     * sent by the client.
     * This value should be 0 if on a normal client.
     */
    public byte opID;

    /**
     * Most recent player this player has pm'd with.
     */
    public Player lastCommunication;

    public Player(Socket client, PacketManager pm) {
        super(client, pm);
        ID = getFreeID();
        this.chat = new Messages(getServer());

        afk = false;
        getServer().getTicker().addTick(this);
    }

    @Override
    public Server getServer() {
        return pm.server;
    }

    /**
     * Get the name of the client the player is using.
     * Browser/Normal client = Minecraft
     * WoM client = WoM
     * Extended Classic compatible client = X (vary's on client)
     * @return
     *        The name of the client
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Set the name of the client the player is using
     * @param name
     *            The name of the client
     */
    public void setClientName(String name) {
        this.clientName = name;
    }

    /**
     * Add an extension this player can use.
     * If the player is not using {@link ClassicClientType#Extend_Classic} protocol, nothing will
     * be added.
     * @param ext
     *           The Extension to add.
     */
    public void addExtension(ClassicExtension ext) {
        if (client != ClassicClientType.Extend_Classic)
            return;
        extend.add(ext);
    }

    /**
     * Get a list of extensions this player can use.
     * @return
     *        An {@link ArrayList} of {@link ClassicExtension}'s
     */
    public final ArrayList<ClassicExtension> getExtensions() {
        return extend;
    }

    /**
     * Check to see if a player has the ability to use an Extension.
     * @param name
     *           The name of the extension
     * @return
     *        returns true if the player can use it, otherwise returns false.
     */
    public boolean hasExtension(String name) {
        for (ClassicExtension c : getExtensions()) {
            if (c.extName().equals(name))
                return true;
        }
        return false;
    }

    /**
     * Ban this player. </br>
     * This method will execute a BanRequest Event. <b>If this server does not
     * have a ban plugin, then the player wont be banned.</b> </br>
     * If the kick parameter is true, then the player will be kicked from the server with the message "Banned:  <b>reason</b>" where reason
     * is specified in the reason parameter.
     * @param banner
     *             The client who is doing the banning.
     * @param reason
     *              The reason for banning this player.
     * @param kick
     *            Whether this player should be kicked as well.
     * @param banip
     *             Whether to ban the player's IP as well.
     */
    public void ban(CommandExecutor banner, String reason, boolean kick, boolean banip) {
        PlayerBanRequestEvent pbre = new PlayerBanRequestEvent(this, reason, kick, banner, banip);
        this.getServer().getEventSystem().callEvent(pbre);
        if (kick)
            kick("Banned: " + reason);
    }

    /**
     * Ban this player.
     * This method will execute a BanRequest Event. If the end-user does not
     * have a ban plugin, then the player wont be banned.
     * @param banner
     *              The client who is doing the banning
     * @param reason
     *              The reason for the ban
     */
    public void ban(CommandExecutor banner, String reason) {
        ban(banner, reason, false, false);
    }

    /**
     * Ban this player.
     * This method will execute a BanRequest Event. If the end-user does not
     * have a ban plugin, then the player wont be banned.
     * The reason provided will be <b>"No reason given"</b>
     * @param banner
     *              The client doing the banning
     */
    public void ban(CommandExecutor banner) {
        ban(banner, "No reason given", false, false);
    }

    /**
     * Ban and kick this player.
     * This method will execute a BanRequest Event. If the end-user does not
     * have a ban plugin, then the player wont be banned.
     * @param banner
     *              The client who is doing the banning
     * @param reason
     *              The reason for the ban
     */
    public void kickBan(CommandExecutor banner, String reason) {
        ban(banner, reason, true, false);
    }

    /**
     * Ban and kick this player.
     * This method will execute a BanRequest Event. If the end-user does not
     * have a ban plugin, then the player wont be banned.
     * The reason provided will be <b>"No reason given"</b>
     * @param banner
     *              The client doing the banning
     */
    public void kickBan(CommandExecutor banner) {
        ban(banner, "No reason given", true, false);
    }

    /**
     * IP Ban this player.
     * A IP Ban will kick the player as well.
     * This method will execute a BanRequest Event. If the end-user does not
     * have a ban plugin, then the player wont be banned.
     * @param banner
     *              The client who is doing the banning
     * @param reason
     *              The reason for the ban
     */
    public void ipBan(CommandExecutor banner, String reason) {
        ban(banner, reason, true, true);
    }

    /**
     * IP Ban this player.
     * A IP Ban will kick the player as well
     * This method will execute a BanRequest Event. If the end-user does not
     * have a ban plugin, then the player wont be banned.
     * The reason provided will be <b>"No reason given"</b>
     * @param banner
     *              The client doing the banning
     */
    public void ipBan(CommandExecutor banner) {
        ban(banner, "No reason given", true, true);
    }

    /**
     * Check to see if a player has the ability to use an Extension.
     * @param class_
     *              The class of Extension
     * @return
     *        True if the player can use it.
     *        Returns false if the player can't use it <b>OR</b> if the class provided was not a {@link ClassicExtension}
     */
    public boolean hasExtension(Class<?> class_) {
        ClassicExtension c = null;
        if ((c = class_.getAnnotation(ClassicExtension.class)) != null)
            return hasExtension(c.extName());
        return false;
    }

    /**
     * Get the current block the client is holding.
     * If the client supports the ClassicExtension protocol, then
     * this value will be updated frequently.
     * If the client does not support the ClassicExtension protocol, then
     * this value will update every time the client makes a block change.
     * @return
     *        The current block the client is holding
     */
    public byte getBlockHolding() {
        return block;
    }

    /**
     * Set the current block the client is holding.
     * @param block
     *             The current block the client is holding
     */
    public void setHoldingBlock(byte iD2) {
        this.block = iD2;
    }

    /**
     * Check to see if a player has the ability to use an Extension.
     * @param object
     *              The Extension object
     * @return
     *        True if the player can use it.
     *        False if the player can't use it <b>OR</b> if the object provided was not a
     *        {@link ClassicExtension}
     */
    public boolean hasExtension(Object object) {
        return hasExtension(object.getClass());
    }

    /**
     * Whether the user is on wom
     * @return
     *        True if the user is using the WoM client
     */
    public boolean isOnWom() {
        return client == ClassicClientType.WoM;
    }


    /**
     * Get the username the client will see above the player's head
     * @return 
     *        The username with the color at the beginning.
     */
    public String getDisplayName() {
        return (prefix != null && !prefix.equals("") ? prefix : "") + (custom_name != null && !custom_name.equals("") && custom_name.startsWith("&") ? "" : color.toString()) + (custom_name != null && !custom_name.equals("") ? custom_name : username); 
    }

    /**
     * Get the custom nick this player is using
     * @return
     *        The custom nick
     */
    public String getCustomName() {
        if (custom_name == null)
            return "";
        return custom_name;
    }

    /**
     * Check Whether or not this player is using a custom
     * nickname
     * @return
     *        True if the player is using a custom nick, false if he isn't
     */
    public boolean isUsingCustomNick() {
        return !getCustomName().equals("");
    }

    /**
     * Give this player a custom nick name to replace his
     * username.
     * 
     * This method will also call {@link Player#respawn()}
     * 
     * The nick assigned will also be saved as a extradata value with the key "mcf_nick"
     * @param nick The new nick
     */
    public void setCustomNick(String nick) {
        nick = ChatColor.convertColorCodes(nick);
        custom_name = nick;
        respawn();
        this.setAttribute("mcf_nick", custom_name);
        this.saveAttribute("mcf_nick");
    }

    /**
     * Remove the custom nickname from this player.
     * This method will also call {@link Player#respawn()}
     */
    public void resetCustomNick() {
        setCustomNick("");
    }

    /**
     * Get the color of the player's username
     * @return
     *        The color
     */
    public ChatColor getDisplayColor() {
        return color;
    }

    /**
     * Get the string that is added before the user's username.
     * @return
     *        The prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Set something to go before the user's username.
     * This can be a title or a star.
     * This prefix wont appear above the players head, unless
     * {@link Player#isShowingPrefix()} is true.
     * 
     * The prefix will appear before the player's username in chat
     * regardless of the value that is returned in {@link Player#isShowingPrefix()}.
     * 
     * Setting the player's prefix using this method will set the raw prefix. If the
     * player has a title color it will be overwritten. Also, this method won't include
     * the title brackets unless they're included in the prefix parameter. To cleanly
     * set a prefix use the method {@link #setCleanPrefix}.
     * 
     * The prefix assigned will also be saved as a extradata value with the key "mcf_prefix"
     * 
     * @param prefix The prefix to set. You should include title brackets here.
     */
    public void setRawPrefix(String prefix) {
        this.prefix = prefix;

        this.setAttribute("mcf_prefix", this.prefix);
        this.saveAttribute("mcf_prefix");
    }

    /**
     * Sets the player's prefix to the specified prefix.
     * This method won't overwrite the player's title color and it will show the player's
     * title brackets.
     * To set the player's raw prefix use {@link #setRawPrefix(String)}
     * 
     * @param prefix - The prefix to set. Title brackets shouldn't be included here.
     */
    public void setCleanPrefix(String prefix) {
        prefix = prefix.replaceAll("%", "&");
        String currprefix = getPrefix();
        if (currprefix == null) {
            currprefix = "[" + prefix + "] ";
        }
        else {
            if (currprefix.startsWith("[")) {
                currprefix = currprefix.substring(1);
            }
            else if (currprefix.startsWith("&") && currprefix.charAt(2) == '[') {
                currprefix = currprefix.substring(0, 2) + currprefix.substring(3, currprefix.length());
            }
            if (currprefix.endsWith("] ")) {
                currprefix = currprefix.substring(0, currprefix.length() - 2);
            }
            if (!currprefix.startsWith("&") || currprefix.length() <= 1) {
                currprefix = "[" + prefix + "] ";
            }
            else {
                String color = currprefix.substring(0, 2);
                if (currprefix.charAt(currprefix.length() - 2) == '&') {
                    currprefix = currprefix.substring(0, currprefix.length() - 2);
                }
                currprefix = color + "[" + prefix + color + "] ";
            }
        }
        setRawPrefix(currprefix);
    }


    /**
     * Whether the user is showing their prefix
     * above their player's head.
     * @return
     *        True if they are, false if they are not.
     */
    public boolean isShowingPrefix() {
        if (getPrefix() == null || getPrefix().equals(""))
            return false;
        return showprefix;
    }

    /**
     * Set Whether or not the user should show their prefix above
     * their player's head.
     * 
     * This method will respawn the player using {@link Player#respawn()}
     * 
     * The boolean assigned will also be saved as a extradata value with the key "mcf_showprefix"
     * @param value
     *             True if they should, false if they should not.
     */
    public void setShowPrefix(boolean value) {
        this.showprefix = value;
        respawn();
        this.setAttribute("mcf_showprefix", value);
        this.saveAttribute("mcf_showprefix");
    }

    /**
     * Set the color for this player.
     * 
     * This method will respawn the player using {@link Player#respawn()}
     * 
     * The color assigned will also be saved as a extradata value with the key "mcf_color"
     * @param color 
     *             The color
     */
    public void setDisplayColor(ChatColor color) {
        this.color = color;
        respawn();
        this.setAttribute("mcf_color", this.color);
        this.saveAttribute("mcf_color");
    }

    /**
     * Respawn this player.
     * This will despawn the user for all the players in the same level
     * and respawn the player.
     */
    public void respawn() {
        despawn();
        spawn();
    }

    /**
     * Despawn this player.
     * In other words, hide this player from all other players
     */
    public void despawn() {
        for (Player p : getServer().getPlayers()) {
            if (p.getLevel() != getLevel())
                continue;
            p.despawn(this);
        }
    }

    /**
     * Despawn this player for all players and despawn all players
     * for this player
     */
    public void completeDespawn() {
        for (Player p : getServer().getPlayers()) {
            if (p.getLevel() != getLevel())
                continue;
            p.despawn(this);
            despawn(p);
        }
    }

    /**
     * Clear the list of players this player can see.
     * This method doesnt despawn any players.
     */
    public void clearSeeableList() {
        seeable.clear();
    }

    /**
     * Spawn this player
     * In other words, show this player to all the other players in the same level
     */
    public void spawn() {
        for (Player p : getServer().getPlayers()) {
            if (p.getLevel() != getLevel() || p == this)
                continue;
            p.spawnPlayer(this);

        }
    }

    /**
     * Spawn this player for all players and spawn all players for this player.
     */
    public void completeSpawn() {
        for (Player p : getServer().getPlayers()) {
            if (p.getLevel() != getLevel() || p == this)
                continue;
            p.spawnPlayer(this);
            spawnPlayer(p);
        }
    }

    /**
     * Verify the player is using a valid account
     * @return Returns true if the account is valid, otherwise it will return false
     */
    public boolean verifyLogin() {
        if (PacketManager.isLocalConnection(getInetAddress()))
            return true;
        return getServer().VerifyNames ? mppass.equals(getRealmppass()) : true;
    }

    /**
     * Get the mppass the user <b>SHOULD</b> have.
     * @return
     *        The mppass the user should have
     */
    public String getRealmppass() {
        try {
            digest.update((String.valueOf(getServer().getClassicSalt()) + username).getBytes());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new BigInteger(1, digest.digest()).toString(16);
    }

    /**
     * Promote this player to the next highest rank </br>
     * If the user already has the highest rank, then nothing happens
     */
    public void promote() {
        final Group gg = getGroup();
        Group lowestabove = null;
        for (Group g : Group.getGroupList()) {
            if (g.permissionlevel > gg.permissionlevel) {
                if (lowestabove == null) { lowestabove = g; }
                if (lowestabove.permissionlevel > g.permissionlevel) { lowestabove = g; }
            }
        }
        if (lowestabove == null)
            return;
        setGroup(lowestabove);
    }

    /**
     * Demote the player to the next lowest rank </br>
     * If the user already has the lowest rank, then nothing happens
     */
    public void demote() {
        final Group gg = getGroup();
        Group highestbelow = null;
        for (Group g : Group.getGroupList()) {
            if (g.permissionlevel < gg.permissionlevel) {
                if (highestbelow == null) { highestbelow = g; }
                if (highestbelow.permissionlevel < g.permissionlevel) { highestbelow = g; }
            }
        }
        if (highestbelow == null) return;
        setGroup(highestbelow);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int amount) {
        this.money = amount;
        setAttribute("mcf_money", this.money);
        saveAttribute("mcf_money");
    }

    /**
     * Returns extra data stored in the player.
     * If the extra data does not exist in the cache, and is <b>NOT</b> compressed in the database, then it will
     * be gotten from the database. If your extra data value is compressed, then please use {@link Player#getCompressedAttribute(String)}, otherwise
     * an exception might be thrown.
     * @param key 
     *           The name of the data
     * @return 
     *        The data that was stored.
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        if (!extra.containsKey(key)) {
            T value = (T)getPlayerAttribute(key, username, getServer());
            extra.put(key, value);
            return (T)value;
        }
        return (T)extra.get(key);
    }

    /**
     * Returns extra data stored in the player.
     * If the extra data does not exist in the cache, and is compressed in the database, then it will be gotten
     * from the database. </br>
     * This method uses more CPU power, it not recommended to use this unless you need to save/get a big object.
     * If this is not the case, then please use {@link Player#getAttribute(String)}
     * @param key 
     *           The name of the data
     * @return 
     *        The data that was stored.
     */
    @SuppressWarnings("unchecked")
    public <T> T getCompressedAttribute(String key) {
        if (!extra.containsKey(key)) {
            T value = (T)getPlayerCompressedAttribute(key, username, getServer());
            extra.put(key, value);
            return (T)value;
        }
        return (T)extra.get(key);
    }

    /**
     * Returns compressed extra data stored in an offline player. </br>
     * This method uses more CPU power, it not recommended to use this unless you need to save/get a big object.
     * If this is not the case, then please use {@link Player#getPlayerAttribute(String, String, Server)}
     * @param key
     *           The name of the data
     * @param username
     *                The username
     * @param server
     *              The server the user belongs to
     * @return
     *         The data that was found, null if nothing was found or in an error occurred while getting the data.
     */
    public static <T> T getPlayerCompressedAttribute(String key, String username, Server server) {
        return getPlayerAttribute(key, username, server, true);
    }

    /**
     * Returns extra data stored in an offline player
     * @param key
     *           The name of the data
     * @param username
     *                The username
     * @param server
     *              The server the user belongs to
     * @return
     *         The data that was found, null if nothing was found or in an error occurred while getting the data.
     */
    public static <T> T getPlayerAttribute(String key, String username, Server server) {
        return getPlayerAttribute(key, username, server, false);
    }

    /**
     * Returns extra data stored in an offline player
     * @param key
     *           The name of the data
     * @param username
     *                The username
     * @param server
     *              The server the user belongs to
     * @return
     *         The data that was found, null if nothing was found or in an error occurred while getting the data.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPlayerAttribute(String key, String username, Server server, boolean compressed) {
        Object object = null;
        T value = null;
        ResultSet r = server.getSQL().fillData("SELECT count(*) FROM " + server.getSQL().getPrefix() + "_extra WHERE name='" + username + "' AND setting='" + key + "'");
        int size = 0;
        try {
            if (server.getSQL() instanceof MySQL)
                r.next();
            size = r.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if (size == 0)
            return null; 
        else {
            r = server.getSQL().fillData("SELECT * FROM " + server.getSQL().getPrefix() + "_extra WHERE name='" + username + "' AND setting='" + key + "'");
            try {
                if (server.getSQL() instanceof MySQL)
                    r.next();
                if (compressed) {
                    ByteArrayInputStream bais;
                    ObjectInputStream ins;
                    bais = new ByteArrayInputStream(r.getBytes("value"));
                    GZIPInputStream gis = new GZIPInputStream(bais);
                    ins = new ObjectInputStream(gis);
                    object = ins.readObject();
                    ins.close();
                    gis.close();
                    bais.close();
                }
                else {
                    ByteArrayInputStream bais;
                    ObjectInputStream ins;
                    bais = new ByteArrayInputStream(r.getBytes("value"));
                    ins = new ObjectInputStream(bais);
                    object = ins.readObject();
                    ins.close(); 
                    bais.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                r.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (object instanceof String && (object.equals("true") || object.equals("false")))
                object = Boolean.parseBoolean((String)object);
            value = (T)object;
            return value;
        }
    }

    /**
     * Check to see if this player has a value stored
     * in extra data or in the SQL table
     * @param key
     *           The key
     * @return
     *        True if the user does have the value, false if he doesn't
     */
    public boolean hasAttribute(String key) {
        if (extra.containsKey(key))
            return true;
        else
            return hasAttribute(key, username, getServer());
    }

    /**
     * Checks to see if an offline player has a value
     * @param key
     *           The key
     * @param username
     *                The username
     * @param server
     *              The server the user belongs to
     * @return
     *        True if the user does have the value, false if he doesn't
     */
    public static boolean hasAttribute(String key, String username, Server server) {
        ResultSet r = server.getSQL().fillData("SELECT count(*) FROM " + server.getSQL().getPrefix() + "_extra WHERE name='" + username + "' AND setting='" + key + "'");
        int size = 0;
        try {
            if (server.getSQL() instanceof MySQL)
                r.next();
            size = r.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        if (size == 0) {
            r = server.getSQL().fillData("SELECT count(*) FROM `mcf_extra` WHERE name='" + username + "' AND setting LIKE '" + key + "_%' ORDER BY setting ASC");
            try {
                if (server.getSQL() instanceof MySQL)
                    r.next();
                size = r.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return size != 0;
        }
        return true;
    }

    /**
     * Store extra data into the player, you can get this data back by
     * using the {@link Player#getAttribute(String)} method
     * @param key 
     *           The name of the data
     * @param object 
     *              The object to save
     */
    public void setAttribute(String key, Object object) {
        removeAttribute(key);
        extra.put(key, object);
    }

    /**
     * Removes an attribute this player has stored. This will also remove the attribute from the SQL table if
     * its saved
     * @param key
     *           The attribute to remove
     */
    public void removeAttribute(String key) {
        if (extra.containsKey(key))
            extra.remove(key);
        PreparedStatement pstmt;
        try {
            pstmt = getServer().getSQL().getConnection().prepareStatement("DELETE FROM " + getServer().getSQL().getPrefix() + "_extra WHERE name = ? AND setting = ?");
            pstmt.setString(1, username);
            pstmt.setString(2, key);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the value of an offline player and save it into the database. The object passed will be serialized and inserted into the database.
     * If the object already exists in the database, then it will execute an UPDATE rather than executing an INSERT
     * @param key
     *           The name of the value
     * @param object
     *              The value
     * @param username
     *                The username to save
     * @param server
     *              The server this user belongs to
     * @throws SQLException
     *                     If there was a problem executing the SQL statement to update/insert
     *                     the object
     * @throws IOException 
     *                    If there was a problem writing the object to the SQL getServer().
     * @throw NotSerializableException
     *      
     */
    public static void setAttribute(String key, Object object, String username, Server server) throws SQLException, IOException, NotSerializableException {
        setAttribute(key, object, username, server, false);
    }

    /**
     * Set the value of an offline player and save it into the database. The object passed will be compressed, serialized and inserted into the database.
     * If the object already exists in the database, then it will execute an UPDATE rather than executing an INSERT. </br>
     * This method uses more CPU power, it not recommended to use this unless you need to save a big object.
     * If this is not the case, then please use {@link Player#setAttribute(String, Object)}
     * @param key
     *           The name of the value
     * @param object
     *              The value
     * @param username
     *                The username to save
     * @param server
     *              The server this user belongs to
     * @throws SQLException
     *                     If there was a problem executing the SQL statement to update/insert
     *                     the object
     * @throws IOException 
     *                    If there was a problem writing the object to the SQL getServer().
     * @throw NotSerializableException
     *      
     */
    public static void setCompressedAttribute(String key, Object object, String username, Server server) throws SQLException, IOException, NotSerializableException {
        setAttribute(key, object, username, server, true);
    }

    /**
     * Set the value of an offline player and save it into the database. The object passed will be serialized and inserted into the database.
     * If the object already exists in the database, then it will execute an UPDATE rather than executing an INSERT
     * @param key
     *           The name of the value
     * @param object
     *              The value
     * @param username
     *                The username to save
     * @param server
     *              The server this user belongs to
     * @param compressed
     *                  Weather the serialized object should be compressed before saving it, this is good for large objects
     * @throws SQLException
     *                     If there was a problem executing the SQL statement to update/insert
     *                     the object
     * @throws IOException 
     *                    If there was a problem writing the object to the SQL getServer().
     * @throw NotSerializableException
     *      
     */
    public static void setAttribute(String key, Object object, String username, Server server, boolean compressed) throws SQLException, IOException, NotSerializableException {
        if (object instanceof Serializable) {
            if (object instanceof Boolean)
                object = ((Boolean)object).toString();
            byte[] data;
            if (compressed) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                GZIPOutputStream gos = new GZIPOutputStream(output);
                ObjectOutputStream out = new ObjectOutputStream(gos);
                out.writeObject(object);
                out.close();
                gos.close();
                data = output.toByteArray();
            }
            else {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(output);
                out.writeObject(object);
                out.close();
                data = output.toByteArray();
            }
            ResultSet r = server.getSQL().fillData("SElECT count(*) FROM " + server.getSQL().getPrefix() + "_extra WHERE name='" + username + "' AND setting='" + key + "'");
            int size = 0;
            if (server.getSQL() instanceof MySQL)
                r.next();
            size = r.getInt(1);
            PreparedStatement pstmt = null;
            if (size == 0) {
                pstmt = server.getSQL().getConnection().prepareStatement("INSERT INTO " + server.getSQL().getPrefix() + "_extra(name, setting, value) VALUES (?, ?, ?)");
                pstmt.setString(1, username);
                pstmt.setString(2, key);
                pstmt.setBytes(3, data);
                pstmt.executeUpdate();
            }
            else {
                pstmt = server.getSQL().getConnection().prepareStatement("UPDATE " + server.getSQL().getPrefix() + "_extra SET value = ? WHERE name = ? AND setting = ?");
                pstmt.setBytes(1, data);
                pstmt.setString(2, username);
                pstmt.setString(3, key);
                pstmt.executeUpdate();
            }
            pstmt.close();
        }
        else
            throw new NotSerializableException("The object that was stored in ExtraData cant be saved because it doesnt implement Serializable!");
    }

    /**
     * Save the value <b>"key"</b> to the database.
     * The object <b>"key"</b> represents will be serialized to the database.
     * If an exception occurs, then the exception will just print to the server log and continue.
     * No exceptions will be raised, if you need to know if an exception was caught, then please use
     * {@link Player#saveAttributeRaised(String)}
     * @param key
     */
    public void saveAttribute(String key) {
        if (!extra.containsKey(key))
            return;
        try {
            saveAttributeRaised(key);
        } catch (Exception e) {
            getServer().logError(e);
        }
    }

    /**
     * Save the value <b>"key"</b> to the database.
     * The object <b>"key"</b> represents will be compressed and serialized to the database using a {@link GZIPOutputStream} object.
     * If an exception occurs, then the exception will just print to the server log and continue.
     * No exceptions will be raised, if you need to know if an exception was caught, then please use
     * {@link Player#saveCompressedAttributeRaised(String)} </br>
     * This method uses more CPU power, it not recommended to use this unless you need to save a big object.
     * If this is not the case, then please use {@link Player#saveAttribute(String)}
     * @param key
     */
    public void saveCompressedAttribute(String key) {
        if (!extra.containsKey(key))
            return;
        try {
            saveCompressedAttributeRaised(key);
        } catch (Exception e) {
            getServer().logError(e);
        }
    }

    /**
     * Save the value <b>key</b> to the database.
     * The object <b>key</b> represents will be serialized to the
     * database.
     * This method will raise an exception when an exception occurs, this is good if you need
     * to know if saving failed or not. Otherwise, use {@link Player#saveAttribute(String)}.
     * @param key
     *           The name of the data to save
     * @throws SQLException
     *                     If there was a problem executing the SQL statement to update/insert
     *                     the object
     * @throws IOException 
     *                    If there was a problem writing the object to the SQL getServer().
     * @throw NotSerializableException
     *                                
     */
    public void saveAttributeRaised(String key) throws SQLException, IOException, NotSerializableException {
        if (!extra.containsKey(key))
            return;
        setAttribute(key, extra.get(key), username, getServer());
    }

    /**
     * Save the value <b>key</b> to the database.
     * The object <b>key</b> represents will be compressed and serialized to the
     * database using a {@link GZIPOutputStream} object.
     * This method will raise an exception when an exception occurs, this is good if you need
     * to know if saving failed or not. Otherwise, use {@link Player#saveCompressedAttribute(String)}. </br>
     * This method uses more CPU power, it not recommended to use this unless you need to save a big object.
     * If this is not the case, then please use {@link Player#saveAttributeRaised(String)}
     * @param key
     *           The name of the data to save
     * @throws SQLException
     *                     If there was a problem executing the SQL statement to update/insert
     *                     the object
     * @throws IOException 
     *                    If there was a problem writing the object to the SQL getServer().
     * @throw NotSerializableException
     *                                
     */
    public void saveCompressedAttributeRaised(String key) throws SQLException, IOException, NotSerializableException {
        if (!extra.containsKey(key))
            return;
        setCompressedAttribute(key, extra.get(key), username, getServer());
    }

    /**
     * Login the player
     * @throws InterruptedException 
     */
    public void login() throws InterruptedException {
        if (isLoggedin)
            return;
        for (int i = 0; i < getServer().getPlayers().size(); i++) {
            Player p = getServer().getPlayers().get(i);
            if ((username.equals(p.username)) && (!p.equals(this))) {
                p.kick("Someone logged in as you!");
            }
        }
        if (Group.getGroup(this) == null)
            setGroup(Group.getDefault());
        sendWelcome();
        final Level level = getServer().getClassicLevelHandler().findLevel(getServer().MainLevel);
        if (level == null) {
            kick("The main level hasnt loaded yet!");
            return;
        }
        changeLevel(level, true);
        loadExtraData();
        getServer().Log(this.username + " has joined the server.");
        chat.serverBroadcast(this.username + " has joined the server.");
        updateAllLists(); //Update me in your list
        isLoggedin = true;
    }

    private void loadExtraData() {
        if (this.hasAttribute("mcf_prefix"))
            this.prefix = (String)getAttribute("mcf_prefix");
        if (this.hasAttribute("mcf_color"))
            this.color = ChatColor.parse((String)getAttribute("mcf_color").toString());
        if (this.hasAttribute("mcf_showprefix"))
            this.showprefix = ((Boolean)(getAttribute("mcf_showprefix"))).booleanValue();
        if (this.hasAttribute("mcf_nick"))
            this.custom_name = getAttribute("mcf_nick");


        final Calendar cal = Calendar.getInstance();
        final String date = dateFormat.format(cal.getTime());
        int login = 0;
        if (hasAttribute("totalLogin"))
            login = ((Integer)(getAttribute("totalLogin"))).intValue();
        login++;
        setAttribute("totalLogin", login);
        saveAttribute("totalLogin");
        if (login == 1) {
            setAttribute("firstLogin", date);
            saveAttribute("firstLogin");
        }
        setAttribute("lastLogin", date);
        saveAttribute("lastLogin");
    }

    /**
     * Get the total number of times this player
     * logged in
     * @return The number of times the player logged in
     */
    public int getTotalLogin() {
        int count = 0;
        if (hasAttribute("totalLogin"))
            count = ((Integer)(getAttribute("totalLogin"))).intValue();
        return count;
    }

    /**
     * Get the data and time this player first logged in
     * with the format "yyyy/MM/dd HH:mm:ss"
     * @return
     *        The date in the format of "yyyy/MM/dd HH:mm:ss"
     */
    public String getFirstLogin() {
        String data = "";
        if (hasAttribute("firstLogin"))
            data = getAttribute("firstLogin");
        return data;
    }

    /**
     * Get the last time the user with the username <b>username</b> logged in
     * with the format "yyyy/MM/dd HH:mm:ss"
     * @param username
     *                The user to lookup
     * @param server
     *              The server this user belongs to
     * @return
     *        The date in the format of "yyyy/MM/dd HH:mm:ss"
     */
    public static String getLastLogin(String username, Server server) {
        String data = "";
        if (hasAttribute("lastLogin", username, server))
            data = getPlayerAttribute("lastLogin", username, server);
        return data;
    }

    /**
     * Get the total number of times this user has been kicked
     * @return
     *        The number of times this user has been kicked
     */
    public int getTotalKicked() {
        int data = 0;
        if (hasAttribute("totalKicked"))
            data = ((Integer)(getAttribute("totalKicked"))).intValue();
        return data;
    }

    /**
     * Get the current group the player is in
     * @return The group
     */
    public Group getGroup() {
        return Group.getGroup(this);
    }

    /**
     * Change the group the player is in
     */
    public void setGroup(Group newgroup) {
        Group old = Group.getGroup(this);
        if (Group.getGroup(this) != null)
            Group.getGroup(this).removePlayer(this);
        newgroup.addPlayer(this);
        if (!this.isLoggedin)
            return;
        if ((old != null && old.isOP && !newgroup.isOP) || (old != null && !old.isOP && newgroup.isOP) || old == null) {
            Packet p = pm.getPacket((byte)0x0f, getClientType());
            p.Write(this, getServer());
        }
        updateAllLists();
    }

    /**
     * Change this player in all ext player's lists.
     */
    private void updateAllLists() {
        for (Player p : getServer().getPlayers()) {
            if (p.hasExtension("ExtAddPlayerName"))
                pm.getPacket((byte)0x33, getClientType()).Write(p, getServer(), this);
        }
    }

    /**
     * Handle the block change packet
     * @param X The X pos where the client modified
     * @param Y The Y pos where the client modified
     * @param Z The Z pos where the client modifed
     * @param type The type of action it did
     * @param holding What the client was holding
     */
    public void handleBlockChange(short X, short Y, short Z, PlaceMode type, byte holding) {
        ClassicBlock getOrginal = (ClassicBlock)level.getTile(X, Y, Z);
        if (holding > 49) {
            kick("Hack Client detected!");
            return;
        }
        PlayerBlockChangeEvent event = new PlayerBlockChangeEvent(this, X, Y, Z, ClassicBlock.getBlock(holding), level, getServer(), type);
        getServer().getEventSystem().callEvent(event);
        if (event.isCancelled()) {
            sendBlockChange(X, Y, Z, getOrginal);
            return;
        }
        ClassicBlock place = event.getBlock();
        if (type == PlaceMode.PLACE)
            GlobalBlockChange(X, Y, Z, place, level, getServer());
        else if (type == PlaceMode.BREAK)
            GlobalBlockChange(X, Y, Z, ClassicBlock.getBlock((byte)0), level, getServer());
    }
    /**
     * Cause this player to place a block in the current level this player is in.
     * You can get the current level by calling {@link Player#getLevel()}.
     * 
     * This method will execute a {@link PlayerBlockChangeEvent} event, if the event is cancelled, then nothing happens.
     * @param x
     *         The block X cord. to place the block
     * @param y
     *         The block Y cord. to place the block
     * @param z
     *         The block Z cord. to place the block
     * @param block
     *             The new block to place
     */
    public void globalBlockChange(int x, int y, int z, ClassicBlock block) {
        globalBlockChange(x, y, z, block, true);
    }

    /**
     * Cause this player to place a block in the current level this player is in.
     * You can get the current level by calling {@link Player#getLevel()}.
     * 
     * This method will execute a {@link PlayerBlockChangeEvent} event, if the event is cancelled, then nothing happens.
     * @param x
     *         The block X cord. to place the block
     * @param y
     *         The block Y cord. to place the block
     * @param z
     *         The block Z cord. to place the block
     * @param block
     *             The new block to place
     * @param updatelevel
     *                   Weather to update the level object or not.
     */
    public void globalBlockChange(int x, int y, int z, ClassicBlock block, boolean updatelevel) {
        PlayerBlockChangeEvent event = new PlayerBlockChangeEvent(this, (short)X, (short)Y, (short)Z, block, level, getServer(), (block.ID == 0 ? PlaceMode.BREAK : PlaceMode.PLACE));
        getServer().getEventSystem().callEvent(event);
        if (event.isCancelled())
            return;
        GlobalBlockChange((short)x, (short)y, (short)z, block, getLevel(), getServer(), updatelevel);
    }

    /**
     * Cause this player to place a series of blocks in the current level this player is in.
     * You can get the current level by calling {@link Player#getLevel()}.
     * 
     * This method will execute a {@link PlayerBlockChangeEvent} event for each {@link BlockUpdate} object in the array passed in the parameter.
     * If an event is canceled, then that block update will be skipped.
     * @param blockupdates
     *                   An array of {@link BlockUpdate} objects that contain the data for each block change.
     */
    public void globalBlockChange(BlockUpdate[] blockupdates) {
        globalBlockChange(blockupdates, true);
    }

    /**
     * Cause this player to place a series of blocks in the current level this player is in.
     * You can get the current level by calling {@link Player#getLevel()}.
     * 
     * This method will execute a {@link PlayerBlockChangeEvent} event for each {@link BlockUpdate} object in the array passed in the parameter.
     * If an event is canceled, then that block update will be skipped.
     * @param blockupdates
     *                   An array of {@link BlockUpdate} objects that contain the data for each block change.
     * @param updatelevel
     *                  Whether or not the level should be updated as well.
     */
    public void globalBlockChange(BlockUpdate[] blockupdates, boolean updatelevel) {
        final SetBlock sb = (SetBlock)getServer().getPacketManager().getPacket((byte)0x05, getClientType());
        ArrayList<byte[]> cache = new ArrayList<byte[]>();
        for (BlockUpdate b : blockupdates) {
            PlayerBlockChangeEvent event = new PlayerBlockChangeEvent(this, (short)b.getX(), (short)b.getY(), (short)b.getZ(), b.getBlock(), level, getServer(), (b.getBlock().ID == 0 ? PlaceMode.BREAK : PlaceMode.PLACE));
            getServer().getEventSystem().callEvent(event);
            if (event.isCancelled())
                continue;
            for (Player p : getServer().getPlayers()) {
                if (p.getLevel() == getLevel())
                    cache.add(sb.getBytes(p, getServer(), (short)b.getX(), (short)b.getY(), (short)b.getZ(), b.getBlock().getVisibleBlock()));
            }
            if (updatelevel)
                getLevel().setTile(b.getBlock(), b.getX(), b.getY(), b.getZ(), getServer());
        }

        for (int pi = 0; pi < getServer().getPlayers().size(); pi++) {
            final Player p = getServer().getPlayers().get(pi);
            for (int i = 0; i < cache.size(); i++) {
                if (p.getLevel() != getLevel())
                    continue;
                try {
                    p.writeData(cache.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        cache.clear();
    }

    /**
     * Send a block update to all the players on level "l" in getServer() "s"
     * @param X The X pos of the udpate
     * @param Y The Y pos of the update
     * @param Z The Z pos of the update
     * @param block The block to send
     * @param l The level the update happened in
     * @param s The getServer() the update happened in
     * @param updateLevel Whether the level should be updated
     */
    public static void GlobalBlockChange(short X, short Y, short Z, ClassicBlock block, Level l, Server s, boolean updateLevel) {
        if (updateLevel) {
           l.setTile(block, X, Y, Z, s);
           block = (ClassicBlock)l.getTile(X, Y, Z); //We dont want to send a ghost block :P
        }
        if (s == null)
            return;
        //Do this way to save on packet overhead
        Packet sb = s.getPacketManager().getPacket("SetBlock");
        for (int i = 0; i < s.getPlayers().size(); i++) {
            final Player p = s.getPlayers().get(i);
            if (p.level == l)
                sb.Write(p, s, X, Y, Z, block.getVisibleBlock());
        }
    }

    /**
     * Update a list of blocks for the level <b>l</b> in the getServer() <b>s</b>
     * @param blockupdates
     *                    The array of block updates to do
     * @param l
     *         The level.
     * @param s
     *         The getServer()
     * @param updateLevel
     *                  Whether the level should be updated
     */
    public static void GlobalBlockChange(BlockUpdate[] blockupdates, Level l, Server s, boolean updateLevel) {
        final SetBlock sb = (SetBlock)s.getPacketManager().getPacket("SetBlock");
        ArrayList<byte[]> cache = new ArrayList<byte[]>();
        for (BlockUpdate b : blockupdates) {
            if (updateLevel) {
                l.setTile(b.getBlock(), b.getX(), b.getY(), b.getZ(), s);
                b.setBlock((ClassicBlock)l.getTile(b.getX(), b.getY(), b.getZ())); //Prevent ghost blocks.
            }
            for (Player p : s.getPlayers()) {
                if (p.getLevel() == l)
                    cache.add(sb.getBytes(p, s, (short)b.getX(), (short)b.getY(), (short)b.getZ(), b.getBlock().getVisibleBlock()));
            }
        }

        for (int pi = 0; pi < s.getPlayers().size(); pi++) {
            final Player p = s.getPlayers().get(pi);
            for (int i = 0; i < cache.size(); i++) {
                if (p.getLevel() != l)
                    continue;
                try {
                    p.writeData(cache.get(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        cache.clear();

    }

    /**
     * Update a list of blocks for the level <b>l</b> in the getServer() <b>s</b>
     * @param blockupdates
     *                    The array of block updates to do
     * @param l
     *         The level.
     * @param s
     *         The getServer()
     */
    public static void GlobalBlockChange(BlockUpdate[] blockupdates, Level l, Server s) {
        GlobalBlockChange(blockupdates, l, s, true);
    }

    /**
     * Send a block to this player
     * @param X The X coord of the block
     * @param Y The Y coord of the block
     * @param Z The Z coord of the block
     * @param block The block to send
     */
    public void sendBlockChange(short X, short Y, short Z, ClassicBlock block) {
        getServer().getPacketManager().getPacket("SetBlock").Write(this, getServer(), X, Y, Z, block.getVisibleBlock());
    }

    /**
     * Send a block update to all the players on level "l" in getServer() "s"
     * @param X The X pos of the udpate
     * @param Y The Y pos of the update
     * @param Z The Z pos of the update
     * @param block The block to send
     * @param l The level the update happened in
     * @param s The getServer() the update happened in
     */
    public static void GlobalBlockChange(short X, short Y, short Z, ClassicBlock block, Level l, Server s) {
        GlobalBlockChange(X, Y, Z, block, l, s, true);
    }

    public static Player find(Server server, String name)
    {
        return server.findPlayer(name);
    }

    /**
     * Get a list of who the player can see
     * @return An ArrayList of players
     */
    public ArrayList<Player> getSeeable() {
        return seeable;
    }

    /**
     * Send the player an MoTD screen
     * You can have the user hang on the MoTD screen when the player first joins
     * @param topline The top line of the MoTD screen
     * @param bottomline The bottom line of the MoTD screen
     */
    public void sendMoTD(String topline, String bottomline) {
        pm.getPacket("MOTD").Write(this, getServer(), topline, bottomline);
    }

    public void updatePos() throws IOException {
        if (!isLoggedin)
            return;

        TP t = (TP)(pm.getPacket("TP"));
        GlobalPosUpdate gps = (GlobalPosUpdate)(pm.getPacket("GlobalPosUpdate"));
        byte[] tosend;
        if (Math.abs(getX() - oldX) >= 127 || Math.abs(getY() - oldY) >= 127 || Math.abs(getZ() - oldZ) >= 127)
            tosend = t.toSend(this);
        else
            tosend = gps.toSend(this);
        Player[] players = getServer().getPlayers().toArray(new Player[getServer().getPlayers().size()]);
        for (Player p : players) {
            if (p == this)
                continue;
            if (p.level == level)
                p.writeData(tosend);
        }
    }

    /**
     * Spawn a new player for this player
     * @param p The player to spawn
     */
    public void spawnPlayer(Player p) {
        if (seeable.contains(p))
            return;
        pm.getPacket("Spawn Player").Write(this, getServer(), p);
        if (this.hasExtension("ExtPlayer") && p.client == ClassicClientType.Extend_Classic)
            pm.getPacket("ExtPlayer").Write(this, getServer(), p);
        seeable.add(p);
    }

    /**
     * Get the level this player is currently on
     * @return The level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Send a new level to the player
     * @param level The level to send
     */
    private void setLevel(Level level) {
        if (this.level == level)
            return;
        this.level = level;
        getServer().Log(username + " moved to " + level.getName());
        levelsender = new SendLevel(this);
        levelsender.start();
    }

    /**
     * Whether or not the player is loading the level
     * @return True if the player is loading the level, false if the player is not
     */
    public boolean isLoading() {
        return levelsender != null;
    }

    /**
     * Wait for the player to finish loading the level
     * The method blocks until the player finishes loading the level 
     * This will block 
     * @throws InterruptedException
     */
    public void waitForLoaded() throws InterruptedException {
        if (levelsender == null)
            return;
        levelsender.join();
    }


    protected void sendWelcome() {
        pm.getPacket("Welcome").Write(this, getServer());
    }

    /**
     * Kick the player from the getServer()
     * @param reason The reason why he was kicked
     */
    public void kick(String reason) {
        PlayerKickedEvent pke = new PlayerKickedEvent(this, reason);
        getServer().getEventSystem().callEvent(pke);
        if (pke.isCancelled()) {
            getServer().Log(username + " kicking has been canceled by a plugin!");
            return;
        }
        if (reason.equals(""))
            reason = "You have been kicked!";
        else
            chat.serverBroadcast(username + " has been kicked (" + reason + ")");
        int kicked = 0;
        if (hasAttribute("totalKicked"))
            kicked = ((Integer)(getAttribute("totalKicked"))).intValue();
        kicked++;
        setAttribute("totalKicked", kicked);
        saveAttribute("totalKicked");
        Packet p = pm.getPacket("Kick");
        this.kickreason = reason;
        p.Write(this, getServer());
    }

    private byte getFreeID() {
        boolean found = true;
        byte toreturn = 0;
        for (int i = 0; i < 255; i++) {
            found = true;
            for (Player p : getServer().getPlayers()) {
                if (p.ID == i) {
                    found = false;
                    break;
                }
            }
            if (found) {
                toreturn = (byte)i;
                break;
            }
        }
        return toreturn;
    }

    /**
     * Get the X cord. of the player
     * This is NOT in block cord.
     * @return The value
     */
    public short getX() {
        return X;
    }

    /**
     * Get the X cord. of the player on the level
     * This is in block cord.
     * @return The value
     */
    public int getBlockX() {
        return X / 32;
    }
    /**
     * Get the X cord. of the player
     * This is NOT in block cord.
     * @return The value
     */
    public short getY() {
        return Y;
    }
    /**
     * Get the X cord. of the player on the level
     * This is in block cord.
     * @return The value
     */
    public int getBlockY() {
        return Y / 32;
    }
    /**
     * Get the ID of the player
     * @return The value
     */
    public byte getID() {
        return ID;
    }
    /**
     * Get the X cord. of the player
     * This is NOT in block cord.
     * @return The value
     */
    public short getZ() {
        return Z;
    }
    /**
     * Get the X cord. of the player on the level
     * This is in block cord.
     * @return The value
     */
    public int getBlockZ() {
        return Z / 32;
    }
    /**
     * Set the current X pos of the player
     * This will NOT teleport the player
     * To teleport the player, use {@link #setPos(short, short, short)}
     * @param value
     */
    public void setX(short value) {
        oldX = X;
        X = value;
    }
    /**
     * Set the current Y pos of the player
     * This will NOT teleport the player
     * To teleport the player, use {@link #setPos(short, short, short)}
     * @param value
     */
    public void setY(short value) {
        oldY = Y;
        Y = value;
    }
    /**
     * Set the current Z pos of the player
     * This will NOT teleport the player
     * To teleport the player, use {@link #setPos(short, short, short)}
     * @param value
     */
    public void setZ(short value) {
        oldZ = Z;
        Z = value;
    }
    /**
     * Teleport the player
     * @param x The X pos to teleport to
     * @param y The Y pos to teleport to
     * @param z The Z pos to teleport to
     * @param yaw The new yaw
     * @param pitch The new pitch
     */
    public void setPos(short x, short y, short z, byte yaw, byte pitch) {
        setX(x);
        setY(y);
        setZ(z);
        oldyaw = this.yaw;
        this.yaw = yaw;
        oldpitch = this.pitch;
        this.pitch = pitch;
        TP();
    }
    /**
     * Teleport the player
     * @param x The X pos to teleport to
     * @param y The Y pos to teleport to
     * @param z The Z pos to teleport to
     */
    public void setPos(short x, short y, short z) {
        setPos(x, y, z, yaw, pitch);
    }

    protected void TP() {
        Packet p = pm.getPacket("TP");
        for (Player pp : getServer().getPlayers()) {
            if (pp.level == level)
                p.Write(pp, pp.getServer(), this, ID); //Tell all the other players as well...
        }
    }

    /**
     * Sends a message to the player.
     * If the message is longer than 64 chars, then it will not send.
     * 
     * @param message
     *               The message to send
     */
    @Override
    public void sendMessage(String message){
        if (!message.equals("") && message.length() > 0) {
            char c = message.charAt(0);
            message = (c == '&' || c == '%') ? message : getServer().defaultColor + message;
        }
        Packet p = pm.getPacket("Message");
        String[] messages = chat.split(message);
        for (String m : messages) {
            this.message = m;
            p.Write(this, getServer());
        }
    }
    /**
     * Sends a wom message to the client if it uses wom.
     * 
     * @param message
     *              The message to put in detail spot.
     */
    public void sendWoMMessage(String message)
    {
        if (client == ClassicClientType.WoM) {
            sendMessage("^detail.user=" + message);
        }
    }
    /**
     * Change the level the player is currently in. This method will
     * call {@link #changeLevel(Level, boolean)} with threading being false.
     * So this method will block until the level sending is finished.
     * @param level The new level the player will be moved to.
     */
    public void changeLevel(Level level) {
        changeLevel(level, false);
    }
    /**
     * Change the level the player is currently in. This method will
     * block if threading is false. If threading is true, level sending will
     * begin in a separate thread and this method wont block.
     * @param level The new level the player will be moved to.
     * @param threading Whether to make this call in a separate thread or not.
     */
    public void changeLevel(Level level, boolean threading)
    {
        if (level == this.level)
            return;
        if (!threading) {
            completeDespawn();
            setLevel(level);
            if (levelsender != null) {
                try {
                    levelsender.join(); //Wait for finish
                } catch (InterruptedException e) { }
            }
            X = (short)((0.5 + level.getSpawnX()) * 32);
            Y = (short)((1 + level.getSpawnY()) * 32);
            Z = (short)((0.5 + level.getSpawnZ()) * 32);
            oldX = X;
            oldY = Y;
            oldZ = Z;
            spawnPlayer(this);
            setPos((short)((0.5 + level.getSpawnX()) * 32), (short)((1 + level.getSpawnY()) * 32), (short)((0.5 + level.getSpawnZ()) * 32));
            completeSpawn();
            PlayerJoinedLevel event = new PlayerJoinedLevel(this, this.level);
            getServer().getEventSystem().callEvent(event);
        }
        else {
            Thread aynct = new asyncLevel(level);
            aynct.start();
        }
    }

    /**
     * This clears the chat screen for the client
     * by sending 20 blank messages
     */
    public void clearChatScreen() {
        for (int i = 0; i < 20; i++)
            sendMessage("");
    }

    public void processCommand(String message)
    {
        message = message.substring(1); //Get rid of the / at the beginning
        if (message.split("\\ ").length > 1)
            getServer().getCommandHandler().execute(this, message.split("\\ ")[0], message.substring(message.indexOf(message.split("\\ ")[1])));
        else
            getServer().getCommandHandler().execute(this, message, "");
    }

    /**
     * Handles the messages a player sends to the getServer(), could be used in the future for run command as player
     * 
     * @param message
     * @return void
     */
    public void recieveMessage(String message){
        if(message.startsWith("/"))
        {
            if (message.startsWith("/womid")) {
                String name = message.substring(7, 15);
                setClientName(name);
                if (name.indexOf("X") != -1 || name.indexOf("x") != -1)
                    client = ClassicClientType.XWoM;
                else
                    client = ClassicClientType.WoM;
                return;
            }
            PlayerCommandEvent event = new PlayerCommandEvent(this, message);
            getServer().getEventSystem().callEvent(event);
            if (event.isCancelled())
                return;
            processCommand(message);
        }else{        
            String formattedMessage = message;
            if(this.cc)
                formattedMessage = ChatColor.convertColorCodes(message);

            PlayerChatEvent event = new PlayerChatEvent(this, message);
            getServer().getEventSystem().callEvent(event);
            if (event.isCancelled())
                return;
            if (formattedMessage.endsWith(">") || formattedMessage.endsWith("<")) {
                formattedMessage = formattedMessage.replaceAll(">", "");
                formattedMessage = formattedMessage.replaceAll("<", "");
                if (!appended_message.equals(""))
                    appended_message += " " + formattedMessage;
                else
                    appended_message = formattedMessage;
                sendMessage(ChatColor.Indigo + "Partial message: " + ChatColor.White + appended_message);
                return;
            }
            else if (!appended_message.equals("")) {
                formattedMessage = appended_message + formattedMessage;
                appended_message = "";
            }
            getServer().Log("User "+ this.username + " sent: " + formattedMessage);
            chat.serverBroadcast(this.getDisplayName() + ChatColor.White + ": " + formattedMessage);
        }
    }

    /**
     * Despawn a player for this player
     * @param p The player to despawn
     */
    public void despawn(Player p) {
        if (!seeable.contains(p))
            return;
        pm.getPacket((byte)0x0c, getClientType()).Write(this, getServer(), p.ID);
        if (p.hasExtension("ExtRemovePlayerName"))
            pm.getPacket((byte)0x35, getClientType()).Write(p, getServer(), this);
        seeable.remove(p);
    }

    /**
     * Close the connection of this client
     * If you want to kick the player, use {@link #kick(String)}
     */
    @Override
    public void closeConnection() {
        if(this.username != null) {
            getServer().Log(this.username + " has left the server.");
            getServer().sendGlobalMessage(this.username + " has left the server.");        
        }
        if (levelsender != null) {
            levelsender.interrupt();
            levelsender = null;
        }
        despawn();
        PlayerDisconnectEvent event = new PlayerDisconnectEvent(this);
        getServer().getEventSystem().callEvent(event);
        super.closeConnection();
        //Clear up data
        extra.clear();
        extend.clear();
        chat = null;
        seeable.clear();
        color = null;
        client = null;
        extra = null;
        extend = null;
        seeable = null;
        lastCommunication = null;
        level = null;
        

        getServer().getTicker().removeTick(this); //Do this last as this takes a while to remove
    }

    protected void finishLevel() {
        levelsender = null;
    }

    protected class asyncLevel extends Thread {

        Level level;

        public asyncLevel(Level level) { this.level = level; }
        @Override
        public void run() {
            changeLevel(level, false);
        }
    }

    protected class SendLevel extends Thread {

        Player p;
        public SendLevel(Player p) { this.p = p; }
        @Override
        public void run() {
            long startTime = System.nanoTime();
            Packet pa;
            pa = pm.getPacket((byte)0x02, getClientType());
            pa.Write(p, getServer());
            if (Thread.interrupted()) {
                p.getServer().Log("Level Sending Aborted", true);
                return;
            }
            pa = null;
            pa = pm.getPacket((byte)0x03, getClientType());
            pa.Write(p, getServer());
            if (Thread.interrupted()) {
                p.getServer().Log("Level Sending Aborted", true);
                return;
            }
            pa = null;
            pa = pm.getPacket((byte)0x04, getClientType());
            pa.Write(p, getServer());
            if (Thread.interrupted()) {
                p.getServer().Log("Level Sending Aborted", true);
                return;
            }
            pa = null;
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            getServer().Log("Loading took: " + duration + "ms");
            if (Thread.interrupted()) {
                p.getServer().Log("Level Sending Aborted", true);
                return;
            }
            p.finishLevel();
        }
    }

    public Messages getChat()
    {
        return chat;
    }

    public boolean isAfk()
    {
        return afk;
    }

    public void setAfk(boolean afk)
    {
        this.afk = afk;
    }
    @Override
    public String getName() {
        return username;
    }

    @Override
    public void tick() {
        Packet pa;
        pa = pm.getPacket((byte)0x01, getClientType());
        pa.Write(this, getServer());
        pa = null;
    }

    @Override
    public boolean inSeperateThread() {
        return true;
    }

    @Override
    public int getTimeout() {
        return 500;
    }
    
    @Override
    public int hashCode() {
        return ID * getName().hashCode();
    }
    
    @Override
    public boolean equals(Object p) {
        if (p instanceof Player) {
            Player player = (Player)p;
            return player.getName().equals(getName()) && player.ID == ID && player.address.equals(address);
        }
        return false;
    }

    @Override
    public String tickName() {
        return getName() + "PingService";
    }
}


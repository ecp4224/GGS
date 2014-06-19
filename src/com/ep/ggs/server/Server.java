/*******************************************************************************
 * Copyright (c) 2013 GGS.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;


import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Warning.WarningState;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;

import com.avaje.ebean.config.ServerConfig;
import com.ep.ggs.API.EventSystem;
import com.ep.ggs.API.action.CmdAbort;
import com.ep.ggs.API.help.Help;
import com.ep.ggs.API.help.HelpItemManager;
import com.ep.ggs.API.io.ServerLogEvent;
import com.ep.ggs.API.plugin.CommandHandler;
import com.ep.ggs.API.plugin.PluginHandler;
import com.ep.ggs.API.server.ServerStartedEvent;
import com.ep.ggs.chat.ChatColor;
import com.ep.ggs.chat.Messages;
import com.ep.ggs.groups.Group;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.iomodel.SMPPlayer;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.sql.ISQL;
import com.ep.ggs.sql.MySQL;
import com.ep.ggs.sql.SQLite;
import com.ep.ggs.system.Console;
import com.ep.ggs.system.PrivilegesHandler;
import com.ep.ggs.system.Serializer;
import com.ep.ggs.system.heartbeat.Beat;
import com.ep.ggs.system.heartbeat.Heart;
import com.ep.ggs.system.heartbeat.MBeat;
import com.ep.ggs.system.heartbeat.WBeat;
import com.ep.ggs.system.ticker.Tick;
import com.ep.ggs.system.ticker.Ticker;
import com.ep.ggs.system.updater.Updatable;
import com.ep.ggs.system.updater.UpdateService;
import com.ep.ggs.system.updater.UpdateType;
import com.ep.ggs.util.FileUtils;
import com.ep.ggs.util.logger.LogInterface;
import com.ep.ggs.util.logger.Logger;
import com.ep.ggs.util.properties.Properties;
import com.ep.ggs.world.ClassicLevelHandler;
import com.ep.ggs.world.Level;
import com.ep.ggs.world.blocks.tracking.BlockTracker;
import com.ep.ggs.world.generator.GeneratorHandler;
import com.ep.ggs.world.generator.classicmodel.FlatGrass;
import com.ep.ggs.world.generator.classicmodel.Forest;
import com.ep.ggs.world.generator.classicmodel.Island;
import com.ep.ggs.world.generator.classicmodel.Mountains;
import com.ep.ggs.world.generator.classicmodel.Ocean;
import com.ep.ggs.world.generator.classicmodel.Pixel;
import com.ep.ggs.world.generator.classicmodel.Rainbow;
import com.ep.ggs.world.generator.classicmodel.Space;
import com.ep.ggs.world.generator.mcmodel.FlatGrassChunk;
import com.ep.ggs.world.mcmodel.ChunkLevel;

public final class Server implements LogInterface, Updatable, Tick, org.bukkit.Server {
    private PacketManager pm;
    private final java.util.logging.Logger log = java.util.logging.Logger.getLogger("GGS");
    private ClassicLevelHandler lm;
    private Logger logger;
    private Ticker ticker;
    private CommandHandler ch;
    private PrivilegesHandler prh;
    private GeneratorHandler gh;
    private UpdateService us;
    private Properties p;
    private ChunkLevel TEMP_MAIN_LEVEL; //TODO Remove this and make an SMPLevelHandler
    private PluginHandler ph;
    private Beat heartbeater;
    private EventSystem es;
    private String Salt;
    private ISQL sql;
    private Console console;
    private Messages m;
    private boolean debug_mode;
    private int oldsize;
    private int cachesize;
    private int cache2size;
    private ArrayList<IOClient> cache;
    private ArrayList<Player> pcache;
    private ArrayList<SMPPlayer> scache;
    private HelpItemManager hmanange;
    private BlockTracker track;

    /**
     * The remote IP of this server. If there was an error finding the remote IP
     * when the server started up, then this value may equal loopback IP (127.0.0.1)
     */
    public static String IP;
    /**
     * The name for currency on this server.
     */
    public String CurrencyName;
    /**
     * The URL hash for this server
     */
    public String hash;
    /**
     * Whether or not to verify names when players connect
     */
    public boolean VerifyNames;

    /**
     * The players currently on the server.
     * @deprecated Use {@link Server#getClassicPlayers()}
     */
    @Deprecated
    public ArrayList<Player> players = new ArrayList<Player>();
    /**
     * Whether the server is running or not
     */
    public boolean Running;

    /**
     * Whether sand will use the new physics or old.
     */
    public boolean newSand;
    /**
     * The port of the server
     */
    public int Port;
    /**
     * How many players are allowed on the server
     */
    public int MaxPlayers;
    /**
     * The name of the server
     */
    public String Name;
    /**
     * The name of the server that will appear on the WoM list
     */
    public String altName;
    /**
     * The description of the server
     */
    public String description;
    /**
     * WoM Flags
     */
    public String flags;
    /**
     * The MoTD of the server (The message the player sees when first joining the server)
     */
    public String MOTD;
    /**
     * The main level (The level the user first joins when the player joins the server)
     */
    public String MainLevel;
    /**
     * Whether or not the server is public
     */
    public boolean Public;

    /**
     * Server's default color.<br>
     * Default color is shown before every message the players receive if it doesn't already
     * have a color code in front 
     */
    public ChatColor defaultColor;
    /**
     * Whether levels will be loaded when /goto is used if they're not already loaded 
     */
    public boolean loadOnGoto = true;
    /**
     * The default filename for the system properties
     */
    public final String configpath = "system.config";
    /**
     * The version of GGS this server runs
     */
    public static final String CORE_VERSION = "1.0.0";
    /**
     * The version of Bukkit this server implements
     */
    public static final String BUKKIT_VERSION = "1.4.7-R1.1-SNAPSHOT";

    /**
     * The version of Minecraft protocol this server implements.
     */
    public static final String PROTOCOL_VERSION = "60";

    /**
     * The version of Minecraft this server supports.
     */
    public static final String MINECRAFT_VERSION = "1.5";
    /**
     * The version number of this GGS server </br>
     * Where 600 would be 6.0.0 </br>
     * And where 600.6 would be 6.0.0b6
     */
    public final double VERSION_NUMBER = 600.6;

    static {
        try {
            URL url = new URL("http://server.GGS.net/ip.php");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            IP = reader.readLine();
        } catch (Exception e) {
            IP = "127.0.0.1";
        }
    }

    /**
     * The handler that handles level loading,
     * level unloading and finding loaded
     * levels
     * @return
     *        The {@link ClassicLevelHandler}
     */
    public final ClassicLevelHandler getClassicLevelHandler() {
        return lm;
    }
    /**
     * Gets the server's generator handler.
     *
     * @return The {@link GeneratorHandler}
     */
    public final GeneratorHandler getGeneratorHandler() {
        return gh;
    }
    /**
     * The handler that handles events.
     * Use the EventSystem to register event or
     * call events
     * @return
     *        The {@link EventSystem}
     */
    public final EventSystem getEventSystem() {
        return es;
    }
    /**
     * The service that helps with commands that implements the {@link HelpItem} interface
     * @return
     */
    public final HelpItemManager getHelpService() {
        return hmanange;
    }
    /**
     * Get the handler that handles the packets
     * @return
     *        The {@link PacketManager}
     */
    public final PacketManager getPacketManager() {
        return pm;
    }
    /**
     * Get the handler that handles the player
     * commands. Use this to add/remove commands
     * or excute commands
     * @return
     *        The {@link CommandHandler}
     */
    public final CommandHandler getCommandHandler() {
        return ch;
    }

    /**
     * Get the default class loader that is used to load
     * plugins and commands, write serialized objects, etc..
     * @return
     *        The default classloader used across the entire server.
     */
    public final ClassLoader getDefaultClassLoader() {
        return getPluginHandler().getClassLoader();
    }
    /**
     * Get the handler that handles the plugins
     * @return
     *        The {@link PluginHandler}
     */
    public final PluginHandler getPluginHandler() {
        return ph;
    }

    /**
     * Get the handler that handles GGS staff privileges
     */
    public final PrivilegesHandler getPrivilegesHandler() {
        return prh;
    }

    /**
     * Get the console object that is controlling the server
     * @return
     *        The {@link Console} object
     */
    public final Console getConsole() {
        return console;
    }
    /**
     * Get the block tracker object that is keeping track of block tracking
     * @return
     *        The {@link BlockTracker} object
     */
    public final BlockTracker getBlockTracker() {
        return track;
    }
    /**
     * The SQL object where you can execute
     * Queries
     * @return
     *        The {@link ISQL} object
     */
    public final ISQL getSQL() {
        return sql;
    }

    public final PrintWriter getLoggerOutput() {
        return getMCFLogger().getWriter();
    }

    /**
     * Get the properties for the {@link Server#configpath} file
     * @return
     *        The {@link Properties} object
     */
    public final Properties getSystemProperties() {
        return p;
    }

    /**
     * Get the object that controls the updating of plugins
     * and other {@link Updatable} objects.
     * @return
     *       The {@link UpdateService} object
     */
    public final UpdateService getUpdateService() {
        return us;
    }

    /**
     * Gets the class that handles messages
     * @return The Message class
     */
    public final Messages getMessages() {
        return m;
    }

    /**
     * Save the system settings
     * @throws IOException
     */
    public void saveSystemSettings() throws IOException {
        getSystemProperties().save("system.config");
    }

    public boolean isLoggingDebugInfo() {
        if (debug_mode)
            return true;
        if (getSystemProperties() == null)
            return false;
        if (!getSystemProperties().hasValue("debug-mode")) {
            getSystemProperties().addSetting("debug-mode", false);
            getSystemProperties().addComment("debug-mode", "Whether GGS should print debug messages to the console.");
        }
        return getSystemProperties().getBool("debug-mode");
    }

    /**
     * The contructor to make a new {@link Server} object
     * @param Name
     *            The default name of the server.
     *            This will be changed if the properties
     *            file has something different.
     * @param Port
     *            The default port of the server.
     *            This will be changed if the properties
     *            file has something different.
     * @param MOTD
     *            The MoTD message for the server.
     *            This will be changed if the properties
     *            file has something different.
     */
    public Server(String Name, int Port, String MOTD) {
        this.Port = Port;
        this.Name = Name;
        this.MOTD = MOTD;
    }

    /**
     * Get the salt.
     * <b>This method can only be called by heartbeaters and the Connect Packet.
     * If this method is called anywhere else, then a {@link IllegalAccessException} is thrown</b>
     * @return
     *        The server Salt
     * @throws IllegalAccessException
     *                               This is thrown when an attempt to call this method
     *                               is invalid.
     */
    public final String getClassicSalt() throws IllegalAccessException {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        try {
            StackTraceElement e = stacks[2]; //The heartbeat class will always be the 3rd in the stacktrace if the heartbeat is being sent correctly
            Class<?> class_ = Class.forName(e.getClassName());
            class_.asSubclass(Heart.class);
            return Salt;
        } catch (ClassNotFoundException e1) { }
        catch (ClassCastException e2) { }
        catch (ArrayIndexOutOfBoundsException e3) { }
        try {
            if (stacks[4].getClassName().equals("com.ep.ggs.networking.packets.classicminecraft.Connect"))
                return Salt;
        }
        catch (ArrayIndexOutOfBoundsException e3) { }
        throw new IllegalAccessException("The salt can only be accessed by the heartbeaters and the Connect packet!");
    }

    /**
     * log an exception to the logger
     * This method will also invoke {@link Throwable#printStackTrace()}
     * @param t The exception to log
     */
    public void logError(Throwable t) {
        t.printStackTrace(getLoggerOutput());
        t.printStackTrace();
    }

    /**
     * Load the server properties such as the server {@link Server#Name}.
     * These properties will always load from the {@link Server#configpath}
     */
    public void loadSystemProperties() {
        Name = getSystemProperties().getValue("Server-Name");
        altName = getSystemProperties().getValue("WOM-Alternate-Name");
        MOTD = getSystemProperties().getValue("MOTD");
        Port = getSystemProperties().getInt("Port");
        MaxPlayers = getSystemProperties().getInt("Max-Players");
        Public = getSystemProperties().getBool("Public");
        description = getSystemProperties().getValue("WOM-Server-description");
        flags = getSystemProperties().getValue("WOM-Server-Flags");
        VerifyNames = getSystemProperties().getBool("Verify-Names");
        newSand = getSystemProperties().getBool("Advanced-Sand");
        CurrencyName = getSystemProperties().getValue("Money-Name");
        loadOnGoto = getSystemProperties().getBool("Load-On-Goto");
        if (getSystemProperties().hasValue("defaultColor"))
            defaultColor = ChatColor.parse(getSystemProperties().getValue("defaultColor"));
        else
            defaultColor = ChatColor.White;
    }

    /**
     * Start the logger.
     * The logger can be started before the server is started, but
     * this method is called in {@link Server#start()}
     */
    public void startLogger() {
        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.HOUR);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        logger = new Logger("logs/" + cal.getTime() + ".txt", this);
        String filename = cal.getTime().toString().replace(" ", "-");
        String finalname = filename.split("-")[0] + "-" + filename.split("-")[1] + "-" + filename.split("-")[2];
        try {
            logger.ChangeFilePath("logs/" , finalname + ".txt");
        } catch (IOException e2) {
            System.out.println("logs/" + finalname + ".txt");
            e2.printStackTrace();
        }
        try {
            logger.Start(false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start the SQL service
     * @param set
     *           The SQL interface to use
     */
    public void startSQL(ISQL set) {
        if (set == null) {
            try {
                sql = (ISQL)Class.forName(getSystemProperties().getValue("SQL-Driver")).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            sql.setPrefix(getSystemProperties().getValue("SQL-table-prefix"));
            if (sql instanceof MySQL) {
                final MySQL mysql = (MySQL)sql;
                mysql.setUsername(getSystemProperties().getValue("MySQL-username"));
                mysql.setPassword(getSystemProperties().getValue("MySQL-password"));
                mysql.setDatabase(getSystemProperties().getValue("MySQL-database-name"));
                mysql.setIP(getSystemProperties().getValue("MySQL-IP"));
                mysql.setPort(Integer.parseInt(getSystemProperties().getValue("MySQL-Port")));
            }
            else if (sql instanceof SQLite) {
                final SQLite sqlite = (SQLite)sql;
                sqlite.setFile(getSystemProperties().getValue("SQLite-File"));
            }
        }
        else
            sql = set;
        sql.connect(this);
        final String[] commands = new String[] {
                "CREATE TABLE if not exists " + sql.getPrefix() + "_extra (name VARCHAR(20), setting TEXT, value LONGTEXT);",
        };
        sql.executeQuery(commands);
        log("SQL all set.");
    }

    /**
     * Start the SQL service
     */
    public void startSQL() {
        startSQL(null);
    }

    /**
     * Start the event system
     */
    public void startEvents() {
        if (es == null)
            es = new EventSystem(this);
    }


    public void startTicker() {
        ticker = new Ticker();
        ticker.startTick();
    }

    /**
     * Start listening to the port specified in {@link Server#Port}
     * and start accepting new connections.
     * @throws IOException
     */
    public void startListening() throws IOException {
        pm = new PacketManager(this);
        pm.startReading();
    }

    //TODO Documentation
    public void start() throws IllegalAccessException {
        start(true, new ServerStartupArgs());
    }

    public void start(ServerStartupArgs args) throws IllegalAccessException {
        start(true, args);
    }

    //TODO Documentation
    public void start(boolean startsql) throws IllegalAccessException {
        start(startsql, new ServerStartupArgs());
    }

    public void start(boolean startsql, ServerStartupArgs args) throws IllegalAccessException {
        Console c = null;
        try {
            StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
            StackTraceElement e = stacks[2];
            Class<?> class_ = Class.forName(e.getClassName());
            c = class_.asSubclass(Console.class).newInstance();
        } catch (Exception e) {
            try {
                StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
                StackTraceElement ste = stacks[3];
                Class<?> class_ = Class.forName(ste.getClassName());
                c = class_.asSubclass(Console.class).newInstance();
            } catch (Exception ee) {
                throw new IllegalAccessException("Server started from outside a console object!");
            }
        }
        start(c, startsql, args);
    }

    public void start(Console c, ServerStartupArgs args) {
        start(c, true, args);
    }

    public void start(Console c, boolean startSQL) {
        start(c, startSQL, new ServerStartupArgs());
    }

    /**
     * Start the server
     */
    public void start(Console console, boolean startSQL, ServerStartupArgs args) {
        if (Running)
            return;
        Running = true;
        startTicker();
        this.console = console;
        console.setServer(this);
        Thread.currentThread().setName("Console-Thread");
        if (args.isLoadingEvents())
            startEvents();
        if (args.isLoadingLogger())
            startLogger();
        log("=============================");
        log("Starting GGS v" + CORE_VERSION);
        if (args.isAllowingSMP()) {
            log("Telling bukkit to use GGS", true);
            Bukkit.setServer(this);
            log("OK!", true);
        }
        if (args.isRunningInDebugMode()) {
            debug_mode = true;
            log("GGS running in debug mode", true);
        }
        log("Starting Command Handler", true);
        ch = new CommandHandler(this);
        log("OK!", true);
        log("Staring Help Service", true);
        hmanange = new HelpItemManager();
        hmanange.init(this);
        log("OK!", true);
        log("Creating default files and directories", true);
        FileUtils.createFilesAndDirs();
        log("OK!", true);
        if (args.isLoadingGroups()) {
            log("Loading groups", true);
            Group.load(this);
            log("OK!", true);
        }
        if (args.isLoadingProperties()) {
            log("Loading Properties", true);
            p = Properties.init(this);
            log("OK!", true);
            if (!p.getBool("Verify-Names")) {
                log("!!WARNING!! You are running the server with verify names off, this means");
                log("anyone can login to the server with any username. Its recommended to turn");
                log("this option on, if you know what your doing, then ignore this message.");
            }
            log("Loading System Settings", true);
            loadSystemProperties();
            log("OK!", true);
        }
        if (args.isLoadingBlockTracking() && args.isAllowingClassic()) {
            log("Loading block tracking", true);
            track = new BlockTracker(this);
            log("OK!", true);
        }
        if (args.isLoadingUpdateService()) {
            log("Loading the Update Service", true);
            us = new UpdateService(this);
            log("OK!", true);
        }
        if (args.isAllowingClassic()) {
            log("Loading the Message Service", true);
            m = new Messages(this);
            log("OK!", true);
        }
        if (args.isLoadingPlugins()) {
            log("Loading Plugin Service", true);
            ph = new PluginHandler(this);
            log("OK!", true);
        }
        if (args.isLoadingGenerator()) {
            log("Loading ClassicGenerator Service", true);
            gh = new GeneratorHandler();
            log("OK!", true);
        }
        log("Attempting to bind to port", true);
        try {
            startListening();
        } catch (IOException e2) {
            e2.printStackTrace();
            log("FAILED!", true);
            return;
        }
        log("OK!", true);
        if (args.isLoadingPrivileges() && args.isAllowingClassic()) {
            log("Loading Privileges Service", true);
            prh = new PrivilegesHandler(this);
            log("OK!", true);
        }
        if (args.isLoadingPlugins()) {
            log("Loading plugins", true);
            ph.loadplugins();
            log("Loaded plugins");
            log("OK!", true);
        }
        /*try {
            if (args.isLoadingPrivileges() && args.isAllowingClassic()) {
                log("Initializing Privilieges Service", true);
                prh.initialize();
                log("OK!", true);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } 
        MCForge Admin Privileges service down, remove code
        */

        if (args.isClassicLoadingLevels() && args.isAllowingClassic()) {
            log("Setting default system wide class loader to " + getDefaultClassLoader(), true);
            Serializer.getKryo().setClassLoader(getDefaultClassLoader());
            log("OK!", true);
            log("Loading Classic Level Service", true);
            lm = new ClassicLevelHandler(this);
            log("OK!", true);
            log("Loading Main Level", true);
            MainLevel = getSystemProperties().getValue("MainLevel");
            if (!new File("levels/" + MainLevel + ".ggs").exists()) {
                lm.newClassicLevel("Main", (short)64, (short)64, (short)64);
                MainLevel = "Main";
            }
            lm.loadClassicLevels();
            log("Loaded levels");
            log("OK!", true);
        }
        /*if (args.isLoadingUpdateService()) {
            log("Adding GGS to Update Service", true);
            us.getUpdateManager().add(this);
            log("OK!", true);
        }
        Update server offline, remove code
         */
        if (args.isAllowingClassic()) {
            log("Creating Classic Salt", true);
            SecureRandom sr = null;
            try {
                sr = SecureRandom.getInstance("SHA1PRNG");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
                return;
            }
            int i = 0;
            for (; i < 100; i++) {
                byte[] seedb = new byte[16];
                sr.nextBytes(seedb);
                Salt = new sun.misc.BASE64Encoder().encode(seedb).replace("=", "" + ((Salt != null) ? Salt.toCharArray()[0] : "A"));
                if (new Random().nextDouble() < .3)
                    break;
            }
            log("Using " + i + " generated salt.", true);
            log("Formatting salt.", true);
            Salt = LetterOrNumber(Salt);
            Salt = Salt.substring(0, 16);
            log("SALT: " + Salt);
            log("OK!", true);
        }
        if (startSQL) {
            log("Starting SQL Service", true);
            startSQL();
            log("OK!", true);
        }
        if (args.isLoadingHeartbeat()) {
            log("Loading Heartbeat Service", true);
            heartbeater = new Beat(this);
            log("OK!", true);
            if (args.isAllowingClassic()) {
                log("Adding Minecraft Heartbeat", true);
                heartbeater.addHeart(new MBeat());
                log("OK!", true);
                log("Adding WOM Heartbeat", true);
                heartbeater.addHeart(new WBeat());
                log("OK!", true);
                /*log("Adding GGS Heartbeat", true);
                heartbeater.addHeart(new ForgeBeat());
                log("OK!", true);
                Heartbeat service down, remove code
                */
            }
            log("Starting Heartbeat Service", true);
            heartbeater.startBeating();
            log("OK!", true);
        }
        log("Created heartbeat");
        log("Server url can be found in 'url.txt'");

        if (args.isLoadingGenerator()) {
            if (args.isAllowingClassic()) {
                log("Adding Flatgrass generator to ClassicGenerator Service", true);
                gh.addGenerator(new FlatGrass(this));
                log("OK!", true);
                log("Adding Forest generator to ClassicGenerator Service", true);
                gh.addGenerator(new Forest(this));
                log("OK!", true);
                log("Adding Island generator to ClassicGenerator Service", true);
                gh.addGenerator(new Island(this));
                log("OK!", true);
                log("Adding Mountains generator to ClassicGenerator Service", true);
                gh.addGenerator(new Mountains(this));
                log("OK!", true);
                log("Adding Ocean generator to ClassicGenerator Service", true);
                gh.addGenerator(new Ocean(this));
                log("OK!", true);
                log("Adding Pixel generator to ClassicGenerator Service", true);
                gh.addGenerator(new Pixel(this));
                log("OK!", true);
                log("Adding Rainbow generator to ClassicGenerator Service", true);
                gh.addGenerator(new Rainbow(this));
                log("OK!", true);
                log("Adding Space generator to ClassicGenerator Service", true);
                gh.addGenerator(new Space(this));
                log("OK!", true);
            }
            gh.addGenerator(new FlatGrassChunk());
        }

        if (args.isLoadingCommands() && args.isAllowingClassic()) {
            log("Adding /abort to Command Service", true);
            getCommandHandler().addCommand(new CmdAbort());
            log("OK!", true);
            log("Adding /help to Command Service", true);
            getCommandHandler().addCommand(new Help());
            log("OK!", true);
        }
        if (args.isLoadingEvents()) {
            log("Creating \"ServerStartedEvent\"", true);
            ServerStartedEvent sse = new ServerStartedEvent(this);
            log("OK!", true);
            log("Calling event", true);
            es.callEvent(sse);
            log("OK!", true);
        }
        log("Adding Server to ticker", true);
        getTicker().addTick(this);
        log("OK!", true);
        if (args.isAllowingSMP()) {
            log("Creating SMP Main Level..");
            TEMP_MAIN_LEVEL = new ChunkLevel(this, "Test");
            log("Generating World..");
            TEMP_MAIN_LEVEL.generateWorld(gh.findGenerator("FlatGrassChunk"));
            log("Done!");
        }
    }

    /**
     * Get all the {@link SimpleIOClient} connected to the server
     * @return
     *        An {@link ArrayList} of {@link SimpleIOClient}
     */
    public ArrayList<IOClient> getClients() {
        return pm.getConnectedClients();
    }

    /**
     * Get all the {@link Player} connected to the server
     * @return
     *         An {@link ArrayList} of {@link Player}
     */
    public synchronized List<Player> getClassicPlayers() {
        if (pcache != null && getClients().equals(cache) && getClients().size() == cachesize)
            return Collections.unmodifiableList(pcache);
        rebuildClassicPlayerCache();
        return Collections.unmodifiableList(pcache);
    }

    /**
     * Rebuild the player cache. </br>
     * The player cache is the list of {@link Player} objects currently connected to the server.
     */
    public synchronized void rebuildClassicPlayerCache() {
        log("Rebuilding Classic Player Cache", true);
        pcache = new ArrayList<Player>();
        for (IOClient i : getClients()) {
            if (i instanceof Player)
                pcache.add((Player)i);
        }
        cache = getClients();
        cachesize = cache.size();
        log("OK!", true);
    }

    /**
     * Get all the {@link SMPPlayer} connected to the server
     * @return
     *         An {@link ArrayList} of {@link SMPPlayer}
     */
    public synchronized List<SMPPlayer> getSMPPlayers() {
        if (scache != null && getClients().equals(cache) && getClients().size() == cache2size)
            return Collections.unmodifiableList(scache);
        rebuildSMPPlayerCache();
        return Collections.unmodifiableList(scache);
    }

    /**
     * Rebuild the player cache. </br>
     * The player cache is the list of {@link SMPPlayer} objects currently connected to the server.
     */
    public synchronized void rebuildSMPPlayerCache() {
        log("Rebuilding SMP Cache", true);
        scache = new ArrayList<SMPPlayer>();
        for (IOClient i : getClients()) {
            if (i instanceof SMPPlayer)
                scache.add((SMPPlayer)i);
        }
        cache = getClients();
        cache2size = cache.size();
        log("OK!", true);
    }

    /**
     * Send a message to all the players on this server
     * @param message
     *               The message to send
     */
    public void sendGlobalMessage(String message) {
        if (m == null)
            return;
        m.serverBroadcast(message);
    }

    /**
     * Send a message to all players on the level <b>world</b>
     * @param message
     *               The message to send
     * @param world
     *             The world to send to
     */
    public void sendWorldMessage(String message, Level world) {
        sendWorldMessage(message, world.getName());
    }

    /**
     * Send a message to all the players on the level with the name <b>world</b>
     * @param message
     *               The message to send
     * @param world
     *             The world name to send to
     */
    public void sendWorldMessage(String message, String world) {
        m.worldBroadcast(message, world);
    }


    /**
     * Search for a player based on the name given.
     * A part of the name will be given and will find
     * the full name and player. If part of the name is given, and
     * more than 1 player is found, then it will return null.
     * @param name
     *            The full/part name of the player
     * @return
     *        The player found. If more than 1 player is found,
     *        then it will return null.
     */
    public Player findPlayer(String name) {
        Player toreturn = null;
        for (int i = 0; i < getClassicPlayers().size(); i++) {
            if (name.equalsIgnoreCase(getClassicPlayers().get(i).getUsername()))
                return getClassicPlayers().get(i);
            else if (getClassicPlayers().get(i).getUsername().toLowerCase().contains(name.toLowerCase()) && toreturn == null)
                toreturn = getClassicPlayers().get(i);
            else if (getClassicPlayers().get(i).getUsername().toLowerCase().contains(name.toLowerCase()) && toreturn != null)
                return null;
        }
        return toreturn;
    }
    private static String LetterOrNumber(String string) {
        final String works = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String finals = "";
        boolean change = true;
        for (char c : string.toCharArray()) {
            for (char x : works.toCharArray()) {
                if (x == c) {
                    change = false;
                    break;
                }
            }
            if (change)
                finals += works.toCharArray()[new Random().nextInt(works.toCharArray().length)];
            else
                finals += c;
            change = true;
        }
        return finals;
    }

    /**
     * Stop the server, this will kick all the players on the server
     * and stop all server services.
     * @throws InterruptedException
     *                             if any thread has interrupted the current thread. The interrupted status of the current thread is cleared when this exception is thrown.
     * @throws IOException
     *                    If there is a problem saving the levels that are loaded
     */
    public void stop() throws InterruptedException, IOException {
        if (!Running)
            return;
        Running = false;
        log("Stopping server...");
        getTicker().removeTick(this);
        for(Player p : players)
        {
            p.sendMessage("Stopping server...");
        }
        for (Level l : this.getClassicLevelHandler().getLevelList()) {
            if (!l.getName().equals(MainLevel))
                l.unload(this);
        }
        if (getClassicLevelHandler().findLevel(MainLevel) != null) {
            getClassicLevelHandler().findLevel(MainLevel).save();
            getClassicLevelHandler().findLevel(MainLevel).unload(this);
        }
        lm.stopBackup();
        ticker.stopTick();
        logger.Stop();
        heartbeater.stopBeating();
        ArrayList<Player> players = new ArrayList<Player>();
        for (Player p : getClassicPlayers())
            players.add(p);
        for (int i = 0; i < players.size(); i++)
            players.get(i).kick("Server shutting down!");
        Properties.reset();
        pm.stopReading();
    }

    /**
     * log something to the logs
     * @param log
     */
    public void log(String log) {
        log(log, false);
    }

    public void log(String log, boolean debug) {
        if (logger == null)
            return;
        if (debug) {
            if (!isLoggingDebugInfo())
                return;
            logger.Log("[DEBUG] " + log);
        }
        else
            logger.Log(log);
    }

    /**
     * Get the logger object
     * @return
     *        The {@link Logger} object
     */
    public Logger getMCFLogger() {
        return logger;
    }

    /**
     * Add a task to be called every 10 milliseconds
     * @param t
     *         The {@link Tick} object to call
     *
     * @deprecated Use {@link Server#getTicker()} and {@link Ticker#addTick(Tick)}
     */
    @Deprecated
    public void Add(Tick t) {
        ticker.addTick(t);
    }

    public Ticker getTicker() {
        return ticker;
    }

    /**
     * Remove a task from the Tick list
     * @param t
     *         The {@link Tick} object to remove
     * @deprecated Use {@link Server#getTicker()} and {@link Ticker#removeTick(Tick)}
     */
    @Deprecated
    public void Remove(Tick t) {
        ticker.removeTick(t);
    }

    @Override
    public void tick() {
        try {
            if (oldsize != getSystemProperties().getKeys().length) {
                saveSystemSettings();
                oldsize = getSystemProperties().getKeys().length;
            }
        } catch (IOException e) {
        }
    }

    @Override
    public boolean inSeperateThread() {
        return false;
    }

    @Override
    public int getTimeout() {
        return 50;
    }

    @Override
    public void onLog(String message) {
        //TODO ..colors?
        ServerLogEvent sle = new ServerLogEvent(this, message, message.split("\\]")[1].trim());
        if (es != null)
            this.es.callEvent(sle);
        System.out.println(message);
    }
    @Override
    public void onError(String message) {
        //TODO ..colors?
        ServerLogEvent sle = new ServerLogEvent(this, message, message.split("\\]")[1].trim());
        if (es != null)
            this.es.callEvent(sle);
        System.out.println(message);
        log.log(java.util.logging.Level.SEVERE, message);
    }
    @Override
    public String getInfoURL() {
        return "http://update.GGS.net/mcf6/updatej";
    }
    @Override
    public String getWebsite() {
        return "www.GGS.net";
    }
    @Override
    public UpdateType getUpdateType() {
        return UpdateType.Manual;
    }
    @Override
    public String getCurrentVersion() {
        return CORE_VERSION;
    }
    @Override
    public String getDownloadPath() {
        return "";
    }
    @Override
    public String getName() {
        return "GGS";
    }
    @Override
    public void unload() {
        throw new RuntimeException("This method should never be called.");
    }
    @Override
    public String tickName() {
        return "";
    }
    @Override
    public Set<String> getListeningPluginChannels() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void sendPluginMessage(Plugin arg0, String arg1, byte[] arg2) {
        // TODO Auto-generated method stub

    }
    @Override
    public boolean addRecipe(Recipe recipe) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public void banIP(String address) {
        // TODO Auto-generated method stub

    }
    @Override
    public int broadcast(String message, String permission) {
        List<SMPPlayer> players = getSMPPlayers();
        int count = 0;

        for (int i = 0; i < players.size(); i++) {
            SMPPlayer p = players.get(i);

            if (p.hasPermission(permission)) {
                p.sendMessage(message);
                count++;
            }
        }

        return count;
    }

    @Override
    public int broadcastMessage(String message) {
        List<SMPPlayer> players = getSMPPlayers();

        for (int i = 0; i < players.size(); i++) {
            players.get(i).sendMessage(message);
        }

        return players.size();
    }
    @Override
    public void clearRecipes() {
        // TODO Auto-generated method stub

    }
    @Override
    public void configureDbConfig(ServerConfig config) {
        // TODO Auto-generated method stub

    }
    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Inventory createInventory(InventoryHolder owner, int size) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Inventory createInventory(InventoryHolder owner, int size,
                                     String title) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public MapView createMap(World world) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public World createWorld(WorldCreator creator) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public boolean dispatchCommand(CommandSender sender, String commandLine)
            throws CommandException {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean getAllowEnd() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean getAllowFlight() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean getAllowNether() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public int getAmbientSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public int getAnimalSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public Set<OfflinePlayer> getBannedPlayers() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getBukkitVersion() {
        return BUKKIT_VERSION;
    }
    @Override
    public Map<String, String[]> getCommandAliases() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public long getConnectionThrottle() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public ConsoleCommandSender getConsoleSender() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public GameMode getDefaultGameMode() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public boolean getGenerateStructures() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public HelpMap getHelpMap() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Set<String> getIPBans() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getIp() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public ItemFactory getItemFactory() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public MapView getMap(short id) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int getMaxPlayers() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public Messenger getMessenger() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int getMonsterSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public String getMotd() {
        return MOTD;
    }
    @Override
    public OfflinePlayer getOfflinePlayer(String name) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public OfflinePlayer[] getOfflinePlayers() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public boolean getOnlineMode() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public org.bukkit.entity.Player[] getOnlinePlayers() {
        return getSMPPlayers().toArray(new SMPPlayer[getSMPPlayers().size()]);
    }
    @Override
    public Set<OfflinePlayer> getOperators() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public org.bukkit.entity.Player getPlayerExact(String name) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public PluginCommand getPluginCommand(String name) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public PluginManager getPluginManager() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int getPort() {
        return Port;
    }
    @Override
    public List<Recipe> getRecipesFor(ItemStack result) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public BukkitScheduler getScheduler() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getServerId() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getServerName() {
        return Name;
    }
    @Override
    public ServicesManager getServicesManager() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getShutdownMessage() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int getSpawnRadius() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public int getTicksPerAnimalSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public int getTicksPerMonsterSpawns() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public String getUpdateFolder() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public File getUpdateFolderFile() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getVersion() {
        return CORE_VERSION;
    }
    @Override
    public int getViewDistance() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public WarningState getWarningState() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public int getWaterAnimalSpawnLimit() {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public Set<OfflinePlayer> getWhitelistedPlayers() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public World getWorld(String name) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public World getWorld(UUID uid) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public File getWorldContainer() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getWorldType() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public List<World> getWorlds() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public boolean hasWhitelist() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean isHardcore() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean isPrimaryThread() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public List<org.bukkit.entity.Player> matchPlayer(String name) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Iterator<Recipe> recipeIterator() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void reload() {
        // TODO Auto-generated method stub

    }
    @Override
    public void reloadWhitelist() {
        // TODO Auto-generated method stub

    }
    @Override
    public void resetRecipes() {
        // TODO Auto-generated method stub

    }
    @Override
    public void savePlayers() {
        // TODO Auto-generated method stub

    }
    @Override
    public void setDefaultGameMode(GameMode mode) {
        // TODO Auto-generated method stub

    }
    @Override
    public void setSpawnRadius(int value) {
        // TODO Auto-generated method stub

    }
    @Override
    public void setWhitelist(boolean value) {
        // TODO Auto-generated method stub

    }
    @Override
    public void shutdown() {
        // TODO Auto-generated method stub

    }
    @Override
    public void unbanIP(String address) {
        // TODO Auto-generated method stub

    }
    @Override
    public boolean unloadWorld(String name, boolean save) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean unloadWorld(World world, boolean save) {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean useExactLoginLocation() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public java.util.logging.Logger getLogger() {
        return log;
    }
    @Override
    public org.bukkit.entity.Player getPlayer(String name) {
        // TODO Auto-generated method stub
        return null;
    }
}

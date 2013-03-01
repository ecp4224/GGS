/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.world.model;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;
import net.mcforge.system.Serializer;
import net.mcforge.system.Serializer.SaveType;
import net.mcforge.system.ticker.Tick;
import net.mcforge.util.FileUtils;
import net.mcforge.util.properties.Properties;
import net.mcforge.world.Level;
import net.mcforge.world.blocks.Block;
import net.mcforge.world.blocks.PhysicsBlock;
import net.mcforge.world.converter.MojangLevel;
import net.mcforge.world.converter.MojangLevelInputStream;
import net.mcforge.world.converter.OldBlocks;
import net.mcforge.world.exceptions.BackupFailedException;
import net.mcforge.world.generator.Generator;

public class ClassicLevel implements Level, Serializable {

    private static final long serialVersionUID = -7297498370800056856L;
    
    private static final Serializer<ClassicLevel> saver = new Serializer<ClassicLevel>(SaveType.GZIP_KRYO);

    private transient Thread physics;

    private boolean run;

    private transient boolean saving;
    
    private transient boolean updated;

    ArrayList<Tick> ticks = new ArrayList<Tick>();

    private boolean autosave = true;

    private int physicsspeed;

	private transient Properties levelprop;

    private boolean unloading;
    
    /**
     * All of the blocks in the level.
     */
    public Block[] blocks;

    /**
     * The width of the level (max X)
     */
    public short width;

    /**
     * The height of the level (max Y)
     */
    public short height;

    /**
     * The depth of the level (max Z)
     */
    public short depth;

    /**
     * The X position (in blocks) where the player spawns.
     */
    public int spawnx;

    /**
     * The Y position (in blocks) where the player spawns.
     */
    public int spawny;

    /**
     * The Z position (in blocks) where the player spawns.
     */
    public int spawnz;

    /**
     * The name of the level
     */
    public String name;

    /**
     * The MoTD for this level
     */
    public String motd = "ignore";

    /**
     * The constructor for {@link ClassicLevel}
     * The constructor wont generate a flat world, you need to
     * call {@link #FlatGrass()}
     * @param width
     *             The width (X) of the level
     * @param height
     *             The height (Y) of the level
     * @param depth
     *             The depth (Z) of the level
     */
    public ClassicLevel(short width, short height, short depth) {
        this();
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.spawnx = width / 2;
        this.spawny = 33;
        this.spawnz = depth / 2;
        blocks = new Block[width*height*depth];
    }

    /**
     * The constructor for the level
     * This constructor starts the physics ticks
     * This constructor wont generate a level, nor will it set a default
     * width, height, and depth. To set a width, height, and depth and
     * initialize the blocks, use must use {@link #ClassicLevel(short, short, short)}
     */
    public ClassicLevel() {
        this.ticks = new ArrayList<Tick>();
    }
    
    @Override
    public void generateWorld(Generator g) {
        if (blocks == null)
            blocks = new Block[width*height*depth];
        g.generate(this);
    }

    @Override
    public void startPhysics(Server server) {
        physics = new Ticker(server);
        run = true;
        physics.start();
    }

    @Override
    public boolean isAutoSaveEnabled() {
        return autosave;
    }

    @Override
    public void setAutoSave(boolean set) {
        autosave = set;
    }

    /**
     * Set a block in this world.
     * If the block is a physicsblock, it will be added
     * to the physics tick.
     * This method wont send out a change to the clients.
     * To do this, use {@link Player#GlobalBlockChange(short, short, short, Block, ClassicLevel, Server)} instead
     * @param b
     *         The block to add
     * @param index
     *             Where to add the block
     * @param server
     *              The server this blockchange is happening in
     */
    public void setTile(Block b, int index, Server server, boolean physics) {
        if (index < 0) index = 0;
        if (index >= blocks.length) index = blocks.length - 1;
        Block wasthere = blocks[index];
        int[] pos = IntToPos(index);
        if (b instanceof PhysicsBlock && physics) {
            blocks[index] = addTick(pos[0], pos[1], pos[2], b, server);
            updated = true;
            return;
        }
        else {
            blocks[index] = b;
            updated = true;
        }
        if(wasthere != null){
            wasthere.onDelete(this, pos[0], pos[1], pos[2], server);
        }
        b.onPlace(this, pos[0], pos[1], pos[2], server);
        if (!physics)
            return;
        try {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0)
                            continue;
                        if (getTile(pos[0] + x, pos[1] + y, pos[2] + z) instanceof PhysicsBlock && !ticks.contains(getTile(pos[0] + x, pos[1] + y, pos[2] + z)))
                            addTick(pos[0] + x, pos[1] + y, pos[2] + z, getTile(pos[0] + x, pos[1] + y, pos[2] + z), server);
                    }
                }
            }
        } catch (Exception e) { }
    }

    @Override
    public PhysicsBlock addTick(int x, int y, int z, Block b, Server server) {
        if (!(b instanceof PhysicsBlock))
            return null;
        PhysicsBlock pb = ((PhysicsBlock)b).clone(server);
        pb.setLevel(this);
        pb.setServer(server);
        pb.setPos(x, y, z);
        if (this.ticks == null)
            this.ticks = new ArrayList<Tick>();
        this.ticks.add(pb);
        return pb;
    }

    /**
     * Check all blocks in the level to see if they require an update
     * <b>WARNING</b> This uses alot of CPU, please only use if absolutly needed.
     * @param server
     *              The server this level belongs to.
     */
    public void checkPhysics(Server server) {
        Thread t = new StartPhysics(server, this);
        t.start();
    }

    @Override
    public void loadProperties() {
        levelprop = new Properties();
        try {
            levelprop.load("levels/properties/" + name + ".properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.physicsspeed = 100;
        if (!levelprop.hasValue("Physics speed"))
            levelprop.addSetting("Physics speed", physicsspeed);
        else
            this.physicsspeed = levelprop.getInt("Physics speed");

        this.motd = "ignore";
        if (!levelprop.hasValue("MOTD"))
            levelprop.addSetting("MOTD", "ignore");
        else
            this.motd = levelprop.getValue("MOTD");    

        try {
            levelprop.save("levels/properties/" + name + ".properties");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Properties getLevelProperties() {
        return levelprop;
    }

    @Override
    public Block getTile(int index) {
        if (index < 0) index = 0;
        if (index >= blocks.length) index = blocks.length - 1;
        if (blocks[index] == null)
            return Block.getBlock((byte)0);
        if (blocks[index].name.equals("NULL"))
            blocks[index] = Block.getBlock("Air");
        return blocks[index];
    }

    @Override
    public Block getTile(int x, int y, int z) {
        return getTile(posToInt(x, y, z));
    }

    @Override
    public int getLength() {
        return blocks.length;
    }

    @Override
    public void setTile(Block b, int x, int y, int z, Server server) {
        setTile(b, posToInt(x, y, z), server, true);
    }

    /**
     * Convert coordinates to a number that will
     * correspond to where the coordinates are in the
     * block array
     * @param x
     *        The X coordinate
     * @param y
     *        The Y coordinate
     * @param z
     *        The Z coordinate
     * @return
     *        The number that will correspond to where the coordinates
     *        are in the block array
     */
    public int posToInt(int x, int y, int z) {
        if (x < 0) { return -1; }
        if (x >= width) { return -1; }
        if (y < 0) { return -1; }
        if (y >= height) { return -1; }
        if (z < 0) { return -1; }
        if (z >= depth) { return -1; }
        return x + z * width + y * width * depth;
    }

    private int[] IntToPos(int index) {
        int[] toreturn = new int[3];
        toreturn[1] = (index / width / depth);
        index -= toreturn[1]*width*depth;
        toreturn[2] = (index/width);
        index -= toreturn[2]*width;
        toreturn[0] = index;
        return toreturn;
    }

    @Override
    public void save() throws IOException {
        if (!new File("levels").exists())
            new File("levels").mkdir();
        saving = true;
        FileOutputStream fos = new FileOutputStream("levels/" + name + ".ggs");
        saver.saveObject(this, fos);
        fos.close();
        saving = false;
        updated = false;
    }

    @Override
    public void unload(Server server) throws IOException {
        unload(server, autosave);
    }
    
    @Override
    public void unload(Server server, boolean save) throws IOException {
        if (save)
            save();
        run = false;
        unloading = true;
        server.Log("[" + name + "] Stopping physics..");
        try {
            physics.interrupt();
            physics.join(5000);
        } catch (InterruptedException e) {
           server.logError(e);
        }
        server.Log("Unloading " + name);
        for (Player p : server.getPlayers()) {
            if (p.getLevel() == this)
                p.changeLevel(server.getLevelHandler().findLevel(server.MainLevel), false);
        }
        blocks = null;
    }
    

    @SuppressWarnings("unchecked")
    @Override
    public void load(String filename, Server server) throws IOException {
        if (new File(filename).isDirectory()) {
            throw new InvalidParameterException("Server tried to load the level \"" + filename + "\" but it's a directory, not a file");
        }
        if (filename.endsWith(".lvl"))
            loadLVL(filename, server);
        else if (filename.endsWith(".dat"))
            convertDat(filename);
        else {
            FileInputStream fis = new FileInputStream(filename);
            ClassicLevel l;
            try {
                l = saver.getObject(fis);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return;
            }
            width = l.width;
            height = l.height;
            autosave = l.autosave;
            blocks = l.blocks;
            if (blocks == null) {
                server.Log(l.name + " BLOCK DATA CORRUPT!");
                blocks = new Block[width*height*depth];
            }
            depth = l.depth;
            motd = l.motd;
            physicsspeed = l.physicsspeed;
            spawnx = l.spawnx;
            spawny = l.spawny;
            spawnz = l.spawnz;
            ticks = (ArrayList<Tick>)l.ticks.clone();
            physics = new Ticker(server);
            name = new File(filename).getName().split("\\.")[0];
            run = true;
            loadProperties();
            unloading = false;
            physics.start();
            saving = false;
            fis.close();
        }
        if (getTile(spawnx, spawny, spawnz) != Block.getBlock("Air") || getTile(spawnx, spawny + 1, spawnz) != Block.getBlock("Air"))
            setNewSpawn(height / 2);
    }
    
    /**
     * Set a new random spawn for this level
     * @param waterlevel
     *                  The minium height for the spawn to be (y)
     */
    public void setNewSpawn(int waterlevel) {
        final Random rand = new Random();
        int tries = 100;
        spawny = waterlevel + (int)(Math.random() * (((waterlevel + 8) - waterlevel) + 1));
        while ((getTile(spawnx, spawny, spawnz).getVisibleBlock() != 0 || getTile(spawnx, spawny + 1, spawnz).getVisibleBlock() != 0 || getTile(spawnx, spawny - 1, spawnz).getVisibleBlock() == 0) && tries > 0) {
            spawnx = rand.nextInt(width);
            spawny = waterlevel + (int)(Math.random() * (((waterlevel + 8) - waterlevel) + 1));
            spawnz = rand.nextInt(depth);
            tries--;
        }
    }
    
    /**
     * Converts a .dat file to a .ggs file
     * @param filename - The filename of the file to load and convert.
     * @param server 
     * @return - The converted level object.
     * @throws IOException - An IOException is thrown if there is a problem reading the file.
     */
    public static ClassicLevel convertDat(String filename) throws IOException{
        FileInputStream fileIn = new FileInputStream(filename);
        GZIPInputStream gzipDecompressor = new GZIPInputStream(fileIn);
        DataInputStream dataInput = new DataInputStream(gzipDecompressor);
        if((dataInput.readInt() != 0x271bb788)) {
            System.out.println("Error! Bad Magic: Invalid .dat file!");
            fileIn.close();
            gzipDecompressor.close();
            dataInput.close();
            return null;
        }
        if((dataInput.readByte() > 2)) {
            System.out.println("Error! Bad Version: .dat ClassicLevel version is greater than 2.");
            fileIn.close();
            gzipDecompressor.close();
            dataInput.close();
            return null;
        }
        ObjectInputStream objectIn = new MojangLevelInputStream(gzipDecompressor);
        try {
            MojangLevel l = (MojangLevel)objectIn.readObject();
            ClassicLevel levelToReturn = getFromMojangLevel(l);
            levelToReturn.name = new File(filename).getName().split("\\.")[0];
            levelToReturn.save();
            new File(filename).delete();
            return levelToReturn;
        } catch (ClassNotFoundException ex) {
            System.out.println(filename + ": Internal Error. Did not find MojangLevel. Cannot convert .dat level!. Report to a Developer!");
        } catch(SecurityException e) {
            e.printStackTrace();
        }
        finally{
            fileIn.close();
            gzipDecompressor.close();
            dataInput.close();
            objectIn.close();
        }
        return null;
    }
    
    /**
     * Converts a MojangLevel to a GGS ClassicLevel.
     * @param m - The MojangLevel to convert.
     * @return  - Converted level!
     */
    private static ClassicLevel getFromMojangLevel(MojangLevel m){
        ClassicLevel l = new ClassicLevel((short)m.width, (short)m.depth, (short)m.height); // TODO: Test this change! Switched Depth and Height
        l.spawnx = m.xSpawn;
        l.spawny = m.zSpawn; // TODO: Test this change! Y to Z
        l.spawnz = m.ySpawn; // TODO: Test this change! Z to Y
        for(int i = 0; i < m.blocks.length; i++){
            l.blocks[i] = Block.getBlock(m.blocks[i]); // Did I do this right?
        }
        return l;
    }
   
    /**
     * Converts a .lvl file to a .ggs file
     * @param file
     *           The file to load and convert
     * @return
     *        The converted level object
     * @throws IOException
     *                   An IOException is thrown if there is a problem reading the file
     */
    public void loadLVL(String file, Server s) throws IOException {
        String name = new File(file).getName().split("\\.")[0];
        File f = new File(file);
        FileInputStream in = new FileInputStream(f);
        GZIPInputStream decompressor = new GZIPInputStream(in);
        DataInputStream data = new DataInputStream(decompressor);
        int magic = convert(data.readShort());

        if (magic != 1874) {
            System.out.println("INVALID .lvl FILE!");
            in.close();
            decompressor.close();
            data.close();
            return;
        }
        //data.read(new byte[16]);
        short width = convert(data.readShort());
        short height = convert(data.readShort());
        short depth = convert(data.readShort());
        this.width = width;
        this.height = height;
        this.depth = depth;
        spawnx = convert(data.readShort());
        spawnz = convert(data.readShort());
        spawny = convert(data.readShort());
        //Ignore these bytes
        data.readByte();
        data.readByte();
        data.readByte();
        data.readByte();

        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = translateBlock(data.readByte(), s);
        }
        data.close();
        try {
            f.delete();
        } catch(SecurityException e) {
            s.logError(e);
        }
        this.name = name;
        save();
    }

    private static short convert(int convert) {
        return (short) (((convert >> 8) & 0xff) + ((convert << 8) & 0xff00));
    }

    private static Block translateBlock(byte id, Server s) {
        if (id == 8)
            return Block.getBlock((byte)9);
        if (id == 10)
            return Block.getBlock((byte)11);
        if (id >= 0 && id <= 49)
            return Block.getBlock(id);
        return Block.getBlock(OldBlocks.convert(id, s));
    }

    private class Ticker extends Thread implements Serializable {
        private static final long serialVersionUID = 1609185967611447514L;
        private transient Server server;
        public Ticker(Server server) { this.server = server; }

        @Override
        public void run() {
            Thread.currentThread().setName(ClassicLevel.this.getName() + "-Physics");
            ArrayList<Tick> toremove = new ArrayList<Tick>();
            while (run) {
                try {
                    if (ticks == null)
                        ticks = new ArrayList<Tick>();
                    for (Tick t : toremove) {
                        ticks.remove(t);
                    }
                    toremove.clear();
                    Tick[] temp = ticks.toArray(new Tick[ticks.size()]);
                    for (Tick t : temp) {
                        if (unloading || saving)
                            break;
                        if (t instanceof PhysicsBlock) {
                            PhysicsBlock pb = (PhysicsBlock)t;
                            if (pb.getLevel() == null)
                                pb.setLevel(ClassicLevel.this);
                            if (pb.getServer() == null)
                                pb.setServer(server);
                            if (pb.runInSeperateThread()) {
                                Thread tt = new Ticker2(pb);
                                tt.start();
                                continue;
                            }
                            if (getTile(pb.getX(), pb.getY(), pb.getZ()).getVisibleBlock() != pb.getVisibleBlock()) {
                                toremove.add(pb);
                                continue;
                            }
                        }
                        if (t != null && !unloading)
                            t.tick();
                    }
                    temp = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(physicsspeed);
                } catch (InterruptedException e) { }
            }
            server.Log("[" + name + "] Physics stopped.");
        }
    }
    
    /**
     * Gets the level's physics speed
     */
    public int getPhysicsSpeed() {
		return physicsspeed;
	}
    
    /**
     * Sets the level's physics speed
     * 
     * @param physicsspeed - the speed to set to
     */
	public void setPhysicsSpeed(int physicsspeed) {
		this.physicsspeed = physicsspeed;
	}
	
    private class Ticker2 extends Thread implements Serializable {
        private static final long serialVersionUID = 1L;

        PhysicsBlock pb;
        public Ticker2(PhysicsBlock pb) { this.pb = pb; }
        @Override
        public void run() {
            if (unloading)
                return;
            pb.tick();
        }
    }

    private class StartPhysics extends Thread implements Serializable {
        private static final long serialVersionUID = 1L;
        transient Server server;
        transient ClassicLevel l;
        public StartPhysics(Server server, ClassicLevel l) { this.l = l; this.server = server; }
        @Override
        public void run() {
            for (int i = 0; i < blocks.length; i++) {
                if (blocks[i] instanceof PhysicsBlock) {
                    PhysicsBlock b = (PhysicsBlock)blocks[i];
                    PhysicsBlock pb = ((PhysicsBlock)b).clone(server);
                    pb.setLevel(l);
                    pb.setServer(server);
                    pb.setPos(l.IntToPos(i));
                    blocks[i] = pb;
                    if (l.ticks == null)
                        l.ticks = new ArrayList<Tick>();
                    l.ticks.add(pb);
                }
            }
        }
    }

    /**
     * @see Level#rawSetTile(int, int, int, Block, Server, boolean)
     * The last parameter is assumed as true.
     */
    public void skipChange(int x, int y, int z, Block block, Server server) {
        skipChange(x, y, z, block, server, true);
    }

    /**
     * @see Level#rawSetTile(int, int, int, Block, Server, boolean)
     */
    public void skipChange(int x, int y, int z, Block block, Server server, boolean addtick) {
        if (x < 0 || y < 0 || z < 0) return;
        if (x >= width || y >= depth || z >= height) return;

        this.setTile(block, posToInt(x, y, z), server, false);
        if (block instanceof PhysicsBlock && addtick)
            skipTick(x, y, z, block, server);
    }

    private void skipTick(int x, int y, int z, Block block, Server server) {
        PhysicsBlock pb = ((PhysicsBlock)block).clone(server);
        pb.setLevel(this);
        pb.setServer(server);
        pb.setPos(x, y, z);
        this.ticks.add(pb);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public int getSpawnX() {
        return spawnx;
    }

    @Override
    public int getSpawnY() {
        return spawny;
    }

    @Override
    public int getSpawnZ() {
        return spawnz;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setWitdh(int width) {
        this.width = (short)width;
    }

    @Override
    public void setHeight(int height) {
        this.height = (short)height;
    }

    @Override
    public void setDepth(int depth) {
        this.depth = (short)depth;
    }

    @Override
    public void setSpawnX(int spawnx) {
        if (spawnx > getWidth())
            this.spawnx = getWidth();
        else if (spawnx < 0)
            this.spawnx = 0;
        else
            this.spawnx = spawnx;
    }

    @Override
    public void setSpawnY(int spawny) {
        if (spawny > getHeight())
            this.spawny = getHeight();
        else if (spawny < 0)
            this.spawny = 0;
        else
            this.spawny = spawny;
    }

    @Override
    public void setSpawnZ(int spawnz) {
        if (spawnz > getDepth())
            this.spawnz = getDepth();
        else if (spawnz < 0)
            this.spawnz = 0;
        else
            this.spawnz = spawnz;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getVersion() {
        return serialVersionUID;
    }

    @Override
    public ArrayList<Tick> getTicks() {
        return ticks;
    }

    @Override
    public void rawSetTile(int x, int y, int z, Block block, Server server, boolean addToTick) {
        this.skipChange(x, y, z, block, server, addToTick);
    }

    @Override
    public void backup(Server server, String location) throws BackupFailedException {
        if (!new File(location + "/" + getName()).exists())
            FileUtils.createChildDirectories(location + "/" + getName());
        long backupnum = new File(location + "/" + getName()).length() + 1;
        new File(location + "/" + getName() + "/" + backupnum).mkdir();
        if (!FileUtils.copyFile("levels/" + getName() + ".ggs", location + "/" + getName() + "/" + backupnum + "/" + getName() + ".ggs"))
            throw new BackupFailedException("Backup for " + getName() + " failed!");
    }

    @Override
    public boolean hasUpdated() {
        return updated;
    }

    @Override
    public List<Block> getBlockList() {
        return Collections.unmodifiableList(Arrays.asList(blocks));
    }
}

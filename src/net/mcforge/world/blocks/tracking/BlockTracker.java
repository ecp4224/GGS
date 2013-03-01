package net.mcforge.world.blocks.tracking;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.API.player.PlayerConnectEvent;
import net.mcforge.API.player.PlayerDisconnectEvent;
import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;
import net.mcforge.system.Serializer;
import net.mcforge.system.Serializer.SaveType;
import net.mcforge.system.ticker.Tick;
import net.mcforge.util.FileUtils;

public class BlockTracker implements Listener, Tick, Serializable {
    private static final long serialVersionUID = 6L;
    private HashMap<Player, ArrayList<BlockData>> cache = new HashMap<Player, ArrayList<BlockData>>();
    private Serializer<ArrayList<BlockData>> saver = new Serializer<ArrayList<BlockData>>(SaveType.GZIP_JAVA);
    private int wait = 60;
    private int oldest = 1;
    private Server server;
    
    public BlockTracker(Server server) {
        this.server = server;
        loadConfig();
        register();
    }
    
    private void register() {
        server.getTicker().addTick(this);
        server.getEventSystem().registerEvents(this);
    }
    
    private void unregister() {
        server.getTicker().removeTick(this);
        PlayerBlockChangeEvent.getEventList().unregister(this);
        PlayerDisconnectEvent.getEventList().unregister(this);
        PlayerConnectEvent.getEventList().unregister(this);
    }
    
    private ArrayList<BlockData> load(Player p) throws IOException, ClassNotFoundException {
        return load(p.getName());
    }
    
    private ArrayList<BlockData> load(String username) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("system/undo_data/" + username + ".mcfu");
        GZIPInputStream gis = new GZIPInputStream(fis);
        ArrayList<BlockData> l = saver.getObject(gis);
        return l;
    }
    
    private boolean save(Player p) {
        if (!cache.containsKey(p))
            return false;
        return save(cache.get(p), p.getName());
    }
    
    private boolean save(ArrayList<BlockData> data, String username) {
        try {
            FileUtils.createChildDirectories("system/undo_data/" + username + ".mcfu");
            FileOutputStream fos = new FileOutputStream("system/undo_data/" + username + ".mcfu");
            GZIPOutputStream gos = new GZIPOutputStream(fos);
            saver.saveObject(data, gos);
            gos.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean hasUndoData(Player p) {
        return hasUndoData(p.getName());
    }
    
    private boolean hasUndoData(String username) {
        return new File("system/undo_data/" + username + ".mcfu").exists();
    }
    
    /**
     * Dispose all the resources of this object.
     * This method will unregister this object from the ticker and any event handlers
     * and check/save all the remaining BlockData left in the cache.
     * 
     * This method will block until saving is complete and the cache has been cleared.
     */
    public void dispose() {
        unregister();
        tick();
        cache.clear();
    }
    
    public void loadConfig() {
        if (server.getSystemProperties().hasValue("blocktracking_save-interval"))
            wait = server.getSystemProperties().getInt("blocktracking_save-interval");
        else {
            server.getSystemProperties().addSetting("blocktracking_save-interval", 60);
            server.getSystemProperties().addComment("blocktracking_save-interval", "How often to save block data to the database (in minutes)");
        }
        
        if (server.getSystemProperties().hasValue("blocktracking_data-size"))
            oldest = server.getSystemProperties().getInt("blocktracking_data-size");
        else {
            server.getSystemProperties().addSetting("blocktracking_data-size", 1);
            server.getSystemProperties().addComment("blocktracking_data-size", "How long to keep undo block data (in days, 1 day being the lowest)");
        }
    }
    
    /**
     * Get the Block Change history of a player with the first element being the current time and the last element being the block change that occurred on
     * the date specified in the parameter <b>date</b> 
     * @param player The player to get the data
     * @param date The lowest date
     * @return The block history in an unmodifiable list with the first date being the one specified in the parameter
     */
    public List<BlockData> getHistory(Player player, Date date) {
        return getHistory(player, date.getTime());
    }
    
    /**
     * Get the block change history of a player with the first element being the current time and the last element being <b>current time - seconds</b> (where seconds is specified in the parameter).
     * @see Date#Date()
     * @param player The player to get the data for
     * @param seconds The amount of seconds to subtract from the current date
     * @return The block history in an unmodifiable list with the last element being the (current time - the seconds specified in the parameter)
     */
    public List<BlockData> getHistory(Player player, int seconds) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, -seconds);
        return getHistory(player, c.getTime().getTime());
    }
    
    /**
     * Get the block change history of a player ending with the lowest value being
     * the millisecond specified in the parameter <b>milliseconds</b>
     * @param player The player to get the data for
     * @param milliseconds The lowest time in milliseconds 
     * @return
     */
    public List<BlockData> getHistory(Player player, long milliseconds) {
        ArrayList<BlockData> data = new ArrayList<BlockData>();
        if (!cache.containsKey(player)) {
            if (hasUndoData(player)) {
                try {
                    data = load(player);
                    cache.put(player, data);
                } catch (IOException e) {
                    e.printStackTrace();
                    return Collections.unmodifiableList(data);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else
                return Collections.unmodifiableList(data);
        }
        else
            data = cache.get(player);
        
        ArrayList<BlockData> toreturn = new ArrayList<BlockData>();
        for (int i = data.size() - 1; i >= 0; i--) {
            final BlockData d = data.get(i);
            if (d.milisecond < milliseconds)
                break;
            toreturn.add(d);
        }
        return Collections.unmodifiableList(toreturn);
    }
    
    /**
     * Get the Block Change history of a player that is offline.
     * With the first element being the current time and the last element being the block change that occurred on
     * the date specified in the parameter <b>date</b> 
     * @param player The player to get the data
     * @param date The lowest date
     * @return The block history in an unmodifiable list with the first date being the one specified in the parameter
     */
    public List<BlockData> getOfflineHistory(String player, Date date) {
        return getOfflineHistory(player, date.getTime());
    }
    
    /**
     * Get the block change history of a player that is offline
     * With the first element being the current time and the last element being <b>current time - seconds</b> (where seconds is specified in the parameter).
     * @see Date#Date()
     * @param player The player to get the data for
     * @param seconds The amount of seconds to subtract from the current date
     * @return The block history in an unmodifiable list with the last element being the (current time - the seconds specified in the parameter)
     */
    public List<BlockData> getOfflineHistory(String player, int seconds) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, -seconds);
        return getOfflineHistory(player, c.getTime().getTime());
    }
    
    /**
     * Get the block change history of a player that is offline
     * With the first element being the current time and the last element being <b>current time - seconds</b> (where seconds is specified in the parameter).
     * @see Date#Date()
     * @param player The player to get the data for
     * @param seconds The amount of seconds to subtract from the current date
     * @return The block history in an unmodifiable list with the last element being the (current time - the seconds specified in the parameter)
     */
    public List<BlockData> getOfflineHistory(String player, long milliseconds) {
        ArrayList<BlockData> data = new ArrayList<BlockData>();
        
        if (hasUndoData(player)) {
            try {
                data = load(player);
            } catch (IOException e) {
                e.printStackTrace();
                return Collections.unmodifiableList(data);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return Collections.unmodifiableList(data);
            }
        }
        else
            return Collections.unmodifiableList(data);
        
        ArrayList<BlockData> toreturn = new ArrayList<BlockData>();
        for (int i = data.size() - 1; i >= 0; i--) {
            final BlockData d = data.get(i);
            if (d.milisecond < milliseconds)
                break;
            toreturn.add(d);
        }
        return Collections.unmodifiableList(toreturn);
    }
    
    private void checkData(Player p) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -oldest);
        long old = c.getTime().getTime();
        ArrayList<BlockData> data = cache.get(p);
        ArrayList<BlockData> toremove = new ArrayList<BlockData>(); //Prevent errors...
        for (BlockData d : data) {
            if (d.milisecond < old)
                toremove.add(d);
        }
        for (BlockData d : toremove) {
            data.remove(d);
        }
        toremove.clear();
    }

    @Override
    public void tick() {
        for (Player player : cache.keySet()) {
            if (!cache.containsKey(player))
                continue;
            checkData(player);
            save(player);
        }
    }

    @Override
    public boolean inSeperateThread() {
        return true;
    }

    @Override
    public int getTimeout() {
        return wait * 60000;
    }
    
    @EventHandler
    public void blockchange(PlayerBlockChangeEvent event) {
        final Player p = event.getPlayer();
        
        BlockData bd = new BlockData();
        bd.x = event.getX();
        bd.y = event.getY();
        bd.z = event.getZ();
        bd.type = event.getPlaceType().getType();
        bd.before = p.getLevel().getTile(bd.x, bd.y, bd.z);
        bd.milisecond = new Date().getTime();
        bd.level = p.getLevel().getName();
        
        if (!cache.containsKey(p)) {
            ArrayList<BlockData> d = new ArrayList<BlockData>();
            d.add(bd);
            cache.put(p, d);
        }
        else {
            ArrayList<BlockData> d = cache.get(p);
            d.add(bd);
        }
    }
    
    @EventHandler
    public void disconnect(PlayerDisconnectEvent event) {
        final Player p = event.getPlayer();
        
        new Thread() {
            @Override
            public void run() {
                save(p);
                cache.remove(p);
            }
        }.start();
    }
    
    @EventHandler
    public void connect(PlayerConnectEvent event) {
        final Player p = event.getPlayer();
        
        if (!hasUndoData(p))
            return;
        
        new Thread() {
            @Override
            public void run() {
                try {
                    cache.put(p, load(p));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public String tickName() {
        return "BlockTrackerService";
    }
}
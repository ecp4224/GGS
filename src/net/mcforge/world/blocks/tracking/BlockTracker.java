package net.mcforge.world.blocks.tracking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.player.PlayerBlockChangeEvent;
import net.mcforge.API.player.PlayerConnectEvent;
import net.mcforge.API.player.PlayerDisconnectEvent;
import net.mcforge.iomodel.Player;
import net.mcforge.server.Server;
import net.mcforge.system.ticker.Tick;

public class BlockTracker implements Listener, Tick {
    private HashMap<Player, ArrayList<BlockData>> cache = new HashMap<Player, ArrayList<BlockData>>();
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
            server.getSystemProperties().addComment("blocktracking_data-size", "How long to keep block data in the database (in days, 1 day being the lowest)");
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
        Date d = new Date();
        long miliseconds = d.getTime();
        miliseconds -= (seconds * 1000);
        return getHistory(player, miliseconds);
    }
    
    /**
     * Get the block change history of a player ending with the lowest value being
     * the millisecond specified in the parameter <b>milliseconds</b>
     * @param player The player to get the data for
     * @param milliseconds The lowest time in milliseconds 
     * @return
     */
    public List<BlockData> getHistory(Player player, long milliseconds) {
        ArrayList<BlockData> data;
        if (!cache.containsKey(player)) {
            if (player.hasAttribute("mcf_blocktracking")) {
                data = player.getCompressedAttribute("mcf_blocktracking");
                cache.put(player, data);
            }
            else
                return new ArrayList<BlockData>();
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
        Date d = new Date();
        long miliseconds = d.getTime();
        miliseconds -= (seconds * 1000);
        return getOfflineHistory(player, miliseconds);
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
        
        if (Player.hasAttribute("mcf_blocktracking", player, server)) {
            data = Player.getPlayerCompressedAttribute("mcf_blocktracking", player, server);
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
        double old = ((((oldest * 24) * 60) * 60) * 1000);
        old = System.currentTimeMillis() - old;
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
            //player.setAttribute("mcf_blocktracking", cache.get(player));
            //player.saveCompressedAttribute("mcf_blocktracking");
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
        
        /*if (cache.containsKey(p)) {
            checkData(p);
            p.setAttribute("mcf_blocktracking", cache.get(p));
            p.saveCompressedAttribute("mcf_blocktracking");
            cache.remove(p);
        }*/
    }
    
    @EventHandler
    public void connect(PlayerConnectEvent event) {
        final Player p = event.getPlayer();
        
        /*if (p.hasAttribute("mcf_blocktracking")) {
            ArrayList<BlockData> data = p.getCompressedAttribute("mcf_blocktracking");
            cache.put(p, data);
            System.out.println("Added " + data.size() + " elements!");
        }*/
    }
}

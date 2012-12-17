/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.system.updater;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import net.mcforge.server.Server;
import net.mcforge.server.Tick;
import net.mcforge.util.FileUtils;

public class UpdateService implements Tick {
    private final UpdateManager um = new UpdateManager();
    private ArrayList<Updatable> queue = new ArrayList<Updatable>();
    private ArrayList<String> restart = new ArrayList<String>();
    private ArrayList<Updatable> ignore = new ArrayList<Updatable>();
    private ArrayList<Updatable> updating = new ArrayList<Updatable>();
    private int wait = 0;
    private Server server;
    private boolean update;
    private UpdateType defaulttype;
    
    /**
     * Create a new UpdateService.
     * @param server
     *              The server the UpdateService belongs to
     */
    public UpdateService(Server server) {
        this.server = server;
        this.server.Add(this);
        defaulttype = UpdateType.Auto_Silent;
        if (this.server.getSystemProperties().hasValue("default_update_type"))
            defaulttype = UpdateType.parse(this.server.getSystemProperties().getValue("default_update_type"));
        else
            this.server.getSystemProperties().addSetting("default_update_type", "auto_silent");
        try {
            load();
        } catch (IOException e) {
            e.printStackTrace(server.getLoggerOutput());
        }
        applyUpdates();
    }

    /**
     * Get the server this UpdateService belongs to
     * @return
     *        The {@link Server} object
     */
    public Server getServer() {
        return server;
    }

    /**
     * Get the {@link UpdateManager}, this object handles all the
     * objects that will be updated.
     * @return
     *        The {@link UpdateManager} object
     */
    public final UpdateManager getUpdateManager() {
        return um;
    }

    /**
     * Check for updates.
     * This will check for updates on all update objects
     */
    public void checkAll() {
        if (update)
            return;
        queue.clear();
        for (int i = 0; i < um.getUpdateObjects().size(); i++) {
            Updatable u = um.getUpdateObjects().get(i);
            if (updating.contains(u))
                continue;
            check(u);
        }
        if (queue.size() > 0)
            update = true;
    }
    
    /**
     * Check for updates on an Updatable object.
     * If the object is already scheduled to update after restart, or scheduled to update
     * at a later time, then nothing happens.
     * @param u
     *         The updatable object to check for updates on
     */
    public void check(Updatable u) {
        if (isInRestartQueue(u) || queue.contains(u) || ignore.contains(u))
            return;
        try {
            if (hasUpdate(u))
                queue.add(u);
        } catch (IOException e) {
            e.printStackTrace(server.getLoggerOutput());
        }
    }
    
    /**
     * Check to see if the Updatable object has any updates.
     * @param u
     *         The updatable object to check
     * @return
     *        True if updates are ready, false if no updates exist
     * @throws IOException
     *                   If there was an error checking for updates.
     */
    public boolean hasUpdate(Updatable u) throws IOException {
        if (!um.checkUpdateServer(u))
            return false;
        boolean toreturn = false;
        URL url = new URL(u.getCheckURL());
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String str;
        while ((str = in.readLine()) != null) {
            if (!str.equals(u.getCurrentVersion()))
                toreturn = true;
        }
        in.close();
        return toreturn;
    }

    /**
     * Update an updatable object.
     * @param u
     *         The object to update
     */
    public void normalUpdate(Updatable u) {
        if (updating.contains(u))
            return;
        Thread t = new Updater(u);
        t.start();
    }
    
    /**
     * Force an updatable object to update
     * @param u
     *         The updatable object to update
     */
    public void forceUpdate(Updatable u) {
        forceUpdate(u, true);
    }
    
    /**
     * Force an updatable object to update
     * @param u
     *         The updatable object to update
     * @param notify
     *             Whether to notify the server or not
     */
    public void forceUpdate(Updatable u, boolean notify) {
        if (notify)
            server.Log("Updating " + u.getDownloadPath() + "..");
        u.unload();
        try {
            downloadFile(u.getDownloadURL(), u.getDownloadPath());
            server.getPluginHandler().loadFile(new File(u.getDownloadPath()));
            if (notify)
                server.Log(u.getDownloadPath() + " has been updated!");
        } catch (IOException e) {
            e.printStackTrace(server.getLoggerOutput());
        }
    }

    @Override
    public void tick() {
        if (wait < 3000 && !update) {
            wait++;
            return;
        }
        wait = 0;
        if (!update)
            checkAll();
        else {
            for (int i = 0; i < queue.size(); i++) {
                normalUpdate(queue.get(i));
            }
            update = false;
        }
    }
    
    /**
     * Remove an updatable object from the restart queue.
     * @param object
     *             The object to remove
     */
    public void removeFromRestartQueue(Updatable object) {
        int index;
        if ((index = getRestartQueueIndex(object)) == -1)
            return;
        restart.remove(index);
    }
    
    public void ignoreUpdate(Updatable object) {
        if (!ignore.contains(object))
            ignore.add(object);
    }
    
    public boolean isUpdateIgnored(Updatable object) {
        return ignore.contains(object);
    }
    
    /**
     * Add an updatable object to the restart queue.
     * This will update the object the next time the server starts up.
     * @param object
     *              The object to add.
     * @param type
     *            The {@link UpdateType} of the Updatable object
     */
    public void addToRestartQueue(Updatable object, UpdateType type) {
        restart.add(object.getDownloadURL() + "@@" + object.getDownloadPath() + "@@" + type.getType());
        if (type == UpdateType.Auto_Notify_Restart)
            server.Log(object.getName() + " will be updated after a restart!");
        save();
    }
    
    /**
     * Add an updatable object to the restart queue.
     * This will update the object the next time the server starts up.
     * @param object
     *             The object to add.
     */
    public void addToRestartQueue(Updatable object) {
        addToRestartQueue(object, object.getUpdateType());
    }
    
    /**
     * Check to see if an updatable object will update after a restart.
     * @param object 
     *              The object check.
     * @return
     *        True if it is, false if it isn't.
     */
    public boolean isInRestartQueue(Updatable object) {
        return isInRestartQueue(object.getDownloadURL());
    }
    
    /**
     * Check to see if a URL is in the restart queue
     * @param dlurl
     *             The URL to check
     * @return
     *       True if it is, false if it isn't.
     */
    public boolean isInRestartQueue(String dlurl) {
        for (String s : restart) {
            if (s.split("\\@@")[0].equals(dlurl))
                return true;
        }
        return false;
    }
    
    private int getRestartQueueIndex(Updatable object) {
        if (!isInRestartQueue(object))
            return -1;
        for (int i = 0; i < restart.size(); i++) {
            if (restart.get(i).split("\\@@")[0].equals(object.getDownloadURL()))
                return i;
        }
        return -1;
    }
    
    private void save() {
        try {
            if (!new File("system").exists())
                new File("system").mkdir();
            FileUtils.createIfNotExist("system/restart.cache");
            PrintWriter out = new PrintWriter("system/restart.cache");
            for (String s : restart) {
                out.println(s);
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(server.getLoggerOutput());
        } catch (IOException e) {
            e.printStackTrace(server.getLoggerOutput());
        }
    }
    
    private void load() throws IOException {
        if (!new File("system/restart.cache").exists())
            return;
        FileInputStream fstream = new FileInputStream("system/restart.cache");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        while ((strLine = br.readLine()) != null)   {
            if (strLine.startsWith("#"))
                continue;
            restart.add(strLine);
        }
        in.close();
        new File("system/restart.cache").delete();
    }
    
    private void applyUpdates() {
        for (String s : restart) {
            if (s.split("\\@@").length != 3)
                continue;
            String url = s.split("\\@@")[0];
            String path = s.split("\\@@")[1];
            UpdateType ut = UpdateType.parse(Integer.parseInt(s.split("\\@@")[2]));
            try {
                downloadFile(url, path);
            } catch (IOException e) {
                e.printStackTrace(server.getLoggerOutput());
            }
            //We dont need to load the plugin, the server will do that.
            if (ut == UpdateType.Auto_Notify_Restart)
                server.Log("Updates for " + path + " have been applied!");
        }
    }
    
    private void downloadFile(String url, String path) throws IOException {
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(path);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
        fos.close();
    }
    
    private class Updater extends Thread {
        
        Updatable u;
        public Updater(Updatable u) { this.u = u; }
        @Override
        public void run() {
            updating.add(u);
            UpdateType type = u.getUpdateType();
            if (type.getType() < defaulttype.getType())
                type = defaulttype;
            if (type == UpdateType.Auto_Silent || type == UpdateType.Auto_Notify)
                forceUpdate(u, type == UpdateType.Auto_Notify);
            else if (type == UpdateType.Auto_Silent_Restart || type == UpdateType.Auto_Notify_Restart)
                addToRestartQueue(u, type);
            else if (type == UpdateType.Ask) {
                if (server.getConsole().askForUpdate(u))
                    forceUpdate(u);
                else
                    ignore.add(u);
            }
            else if (type == UpdateType.Manual)
                server.getConsole().alertOfManualUpdate(u);
            updating.remove(u);
        }
    }
}


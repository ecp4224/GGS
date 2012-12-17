/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.system.heartbeat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import net.mcforge.server.Server;

public class Beat extends Thread {
    private Server server;

    private ArrayList<Heart> hearts = new ArrayList<Heart>();
    
    private boolean running;

    /**
     * Create a new Beater
     * @param server
     *             The server this beater will
     *             beat for.
     */
    public Beat(Server server) {
        this.server = server;
    }

    /**
     * Add a heart to beat
     * @param h
     *         The {@link Heart} object
     */
    public void addHeart(Heart h) {
        synchronized(hearts) {
            if (!hearts.contains(h))
                hearts.add(h);
        }
    }
    
    /**
     * Remove a heart currently beating.
     * @param h
     *         The {@link Heart} object
     */
    public void removeHeart(Heart h) {
        synchronized(hearts) {
            if (hearts.contains(h))
                hearts.remove(h);
        }
    }
    
    /**
     * Start beating. If the beater already started beating, then
     * nothing will happen.
     */
    public void startBeating() {
        if (running)
            return;
        running = true;
        super.start();
    }
    
    /**
     * Stop beating. If the beater is already stopped, then
     * nothing will happen.
     */
    public void stopBeating() {
        if (!running)
            return;
        running = false;
    }

    /**
     * Get the server this beater is beating for.
     * @return
     *        The {@link Server} object
     */
    public Server getServer() {
        return server;
    }

    /**
     * Set the server this beater is beating for.
     * @param server
     *              The {@link Server} object
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * Get an {@link ArrayList} of hearts currently
     * beating.
     * @return
     *        An {@link ArrayList} of hearts
     */
    public ArrayList<Heart> getHearts() {
        return hearts;
    }

    /**
     * Weather or not this beater is running
     * @return
     *        Returns true if the beater is running, otherwise it will
     *        return false.
     */
    public boolean isRunning() {
        return running;
    }
    
    @Override
    public void run() {
        URL url;
        HttpURLConnection connection = null;
        BufferedReader reader;
        byte[] data;
        while (isRunning()) {
            synchronized(getHearts()) {
                for (Heart h : getHearts()) {
                    try {
                        URL u = new URL(h.getURL());
                        HttpURLConnection con = (HttpURLConnection)u.openConnection();
                        con.connect();

                        if(con.getResponseCode() == HttpURLConnection.HTTP_OK)
                        {
                            url = new URL(h.getURL() + "?" + h.Prepare(getServer()));
                            connection = (HttpURLConnection)url.openConnection();
                            data = h.Prepare(getServer()).getBytes();
                            connection.setDoOutput(true);
                            connection.setRequestProperty("Content-Lenght", String.valueOf(data.length));
                            connection.setUseCaches(false);
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            connection.connect();
                            try {
                                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                try {
                                    h.onPump(reader, getServer());
                                } catch (Exception e) {
                                    e.printStackTrace(server.getLoggerOutput());
                                }
                            } catch (Exception e) {
                                e.printStackTrace(server.getLoggerOutput());
                                getServer().Log("Error pumping " + h.getURL() + " heart!");
                            }
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace(server.getLoggerOutput());
                    } catch (IOException e) {
                        System.out.println("Unable to connect to " + h.getURL() + "!");
                    } finally {
                        if (connection != null)
                            connection.disconnect();
                    }
                }
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


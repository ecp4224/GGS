/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.server;

public class ServerStartupArgs {
    private boolean events;
    private boolean logger;
    private boolean commands;
    private boolean groups;
    private boolean properties;
    private boolean update;
    private boolean plugins;
    private boolean privileges;
    private boolean levels;
    private boolean heartbeat;
    private boolean generator;
    private boolean tracker;
    private boolean debug; 
    private boolean classic;
    private boolean smp;
    
    public ServerStartupArgs() {
        setLoadingEvents(true);
        setLoadingLogger(true);
        setLoadingCommands(true);
        setLoadingGroups(true);
        setLoadingProperties(true);
        setLoadingUpdateService(true);
        setLoadingPlugins(true);
        setLoadingPrivileges(true);
        setLoadingClassicLevels(true);
        setLoadingHeartbeat(true);
        setLoadingGenerator(true);
        setIsLoadingBlockTracking(true);
        setAllowClassic(true);
        setAllowSMP(true);
    }
    
    /**
     * Whether this server will allow SMP clients to connect.
     * @return
     */
    public boolean isAllowingSMP() {
        return smp;
    }
    
    /**
     * Set whether the server should allow SMP clients or not.
     * @param allow
     * @return
     */
    public void setAllowSMP(boolean allow) {
        this.smp = allow;
    }
    
    /**
     * Whether this server will allow classic clients to connect.
     * @return
     */
    public boolean isAllowingClassic() {
        return classic;
    }
    
    /**
     * Set whether the server should allow classic clients or not.
     * @param allow
     * @return
     */
    public void setAllowClassic(boolean allow) {
        this.classic = allow;
    }
    
    /**
     * Whether or not the server will run in debug mode or not.
     * @return
     */
    public boolean isRunningInDebugMode() {
        return debug;
    }
    
    /**
     * Set whether or not the server will run in debug mode or not.
     */
    public void runInDebugMode(boolean debug) {
        this.debug = debug;
    }
    
    /**
     * Return if the server will load the block tracker.
     * @return
     */
    public boolean isLoadingBlockTracking() {
        return tracker;
    }
    
    /**
     * Set whether or not the server will load the block tracker.
     * @param tracker
     */
    public void setIsLoadingBlockTracking(boolean tracker) {
        this.tracker = tracker;
    }

    /**
     * Returns if the server is loading the event handler.
     * @return
     */
    public boolean isLoadingEvents() {
        return events;
    }

    /**
     * Set if you want the server to load the event handler.
     * @param events
     */
    public void setLoadingEvents(boolean events) {
        this.events = events;
    }

    /**
     * Return whether or not the server is loading the logger
     * @return the logger
     */
    public boolean isLoadingLogger() {
        return logger;
    }

    /**
     * Set whether or not you want the server to load the logger
     * @param logger the logger to set
     */
    public void setLoadingLogger(boolean logger) {
        this.logger = logger;
    }

    /**
     * Return whether or not the server is loading the command handler
     * @return the commands
     */
    public boolean isLoadingCommands() {
        return commands;
    }

    /**
     * Set whether or not you want the server to load commands
     * @param commands the commands to set
     */
    public void setLoadingCommands(boolean commands) {
        this.commands = commands;
    }

    /**
     * Return whether or not the server is loading groups
     * @return the groups
     */
    public boolean isLoadingGroups() {
        return groups;
    }

    /**
     * Set whether or not you want the server to load groups
     * @param groups the groups to set
     */
    public void setLoadingGroups(boolean groups) {
        this.groups = groups;
    }

    /**
     * Return whether or not the server will load system properties
     * @return the properties
     */
    public boolean isLoadingProperties() {
        return properties;
    }

    /**
     * Set whether or not the server is loading system properties
     * @param properties the properties to set
     */
    public void setLoadingProperties(boolean properties) {
        this.properties = properties;
    }

    /**
     * Return whether or not the server is loading the update service
     * @return the update
     */
    public boolean isLoadingUpdateService() {
        return update;
    }

    /**
     * Set whether or not you want the update service to load
     * @param update the update to set
     */
    public void setLoadingUpdateService(boolean update) {
        this.update = update;
    }

    /**
     * Return whether or not the server is loading plugins
     * @return the plugins
     */
    public boolean isLoadingPlugins() {
        return plugins;
    }

    /**
     * Set whether or not the server will load plugins
     * @param plugins the plugins to set
     */
    public void setLoadingPlugins(boolean plugins) {
        this.plugins = plugins;
    }

    /**
     * Return whether or not the server is loading dev protection
     * @return the privileges
     */
    public boolean isLoadingPrivileges() {
        return privileges;
    }

    /**
     * Set whether or not the server will load dev protection
     * @param privileges the privileges to set
     */
    public void setLoadingPrivileges(boolean privileges) {
        this.privileges = privileges;
    }

    /**
     * Return whether or not the server is loading levels
     * @return the levels
     */
    public boolean isClassicLoadingLevels() {
        return levels;
    }

    /**
     * Set whether or not the server will load levels
     * @param levels the levels to set
     */
    public void setLoadingClassicLevels(boolean levels) {
        this.levels = levels;
    }

    /**
     * Return whether or not the server will load the heartbeater
     * @return the heartbeat
     */
    public boolean isLoadingHeartbeat() {
        return heartbeat;
    }

    /**
     * Set whether or not the server will load the heartbeater
     * @param heartbeat the heartbeat to set
     */
    public void setLoadingHeartbeat(boolean heartbeat) {
        this.heartbeat = heartbeat;
    }

    /**
     * Return whether or not the server will load the generator service
     * @return the generator
     */
    public boolean isLoadingGenerator() {
        return generator;
    }

    /**
     * Set whether or not the server will load the generator service
     * @param generator the generator to set
     */
    public void setLoadingGenerator(boolean generator) {
        this.generator = generator;
    }
}

/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.system.updater;


/**
 * An updatable object can be automatically updated
 * by the core. You must provide the following:
 * <br><br>
 * <b>A info URL</b>
 * This url will return, in json, all updates that this object contains. </br>
 * An example can be found below: </br>
 *  {"updates": </br>
 *       [{"uid":"1","version":"1.1.0","download_url":"http:\/\/www.mywebsite.com\/downloads\/update","minimum_core_version":"1.0.0b7"}, </br>
 *        {"uid":"2","version":"1.2.0","download_url":"http:\/\/www.mywebsite.com\/downloads\/update","minimum_core_version":"1.0.0b8"}]}       
 *<br>
 *<b>A Download URL</b>
 *This url will return a jar file of the latest version.
 *<br>
 *<b>Update Type</b>
 *This is the how the object will update, see {@link UpdateType} for more info.
 *<br>
 *<b>Current Version</b>
 *This will return, in a String, the current version of this object.
 *<br>
 *<b>Unload Method</b>
 *This will unload the object from memory. For plugins, {@link PluginHandler#unload(Plugin)} will do the trick.
 *For commands, {@link CommandHandler#removeCommand(Command)} will unload the object.
 *
 */
public interface Updatable {

    /**
     * The URL that contains the json information about all updates for this object. </br>
     * Example URL: </br>
     *        <code>return "http://www.mcforge.net/curversion.txt";</code> </br>
     * Example Content: </br>
     *        {"updates": </br>
     *       [{"uid":"1","version":"1.1.0","download_url":"http:\/\/www.mywebsite.com\/downloads\/update","minimum_core_version":"1.0.0b7"}, </br>
     *        {"uid":"2","version":"1.2.0","download_url":"http:\/\/www.mywebsite.com\/downloads\/update","minimum_core_version":"1.0.0b8"}]}       
     * @return
     *        The URL.
     */
    public String getInfoURL();

    /**
     * The website to go to for manual updates.
     * @return
     *        The website URL
     */
    public String getWebsite();

    /**
     * The type this object will do.
     * These can be found in {@link UpdateType}
     * If the default update type set by the user is higher, then
     * that will be used instead.
     * Example:
     *         {@link UpdateType#Auto_Silent} is less than {@link UpdateType#Auto_Notify}
     * @return
     *        The type of update.
     */
    public UpdateType getUpdateType();

    /**
     * Get the current version of this object
     * @return
     *        The current version.
     */
    public String getCurrentVersion();

    /**
     * The path this object will be updated and loaded again
     * @return
     *        The path.
     *        Example:
     *                <code>return "plugins/Test.jar";</code>
     */
    public String getDownloadPath();

    /**
     * The name of this updatable object. This can return
     * {@link Plugin#getName()} or just return a normal string
     * @return
     *        The updatable object name.
     */
    public String getName();

    /**
     * This method will unload the object before it gets
     * updated. For plugins, you would call the {@link PluginHandler#unload(Plugin)} method.
     */
    public void unload();
}


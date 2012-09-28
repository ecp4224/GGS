package net.mcforge.system.updater;

import net.mcforge.API.plugin.Plugin;
import net.mcforge.API.plugin.PluginHandler;

public interface Updatable {
	
	/**
	 * The URL that contains the latest version number as a string.
	 * Example:
	 *        <code>return "http://www.mcforge.net/curversion.txt";</code>
	 * @return
	 *        The URL.
	 */
	public String getCheckURL();
	
	/**
	 * The URL that leads to a direct download of the newest version.
	 * Example:
	 *         <code>return "http://www.mcforge.net/MCForge.jar";</code>
	 * @return
	 *        The URL.
	 */
	public String getDownloadURL();
	
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
	 * This method will unload the object before it gets
	 * updated. For plugins, you would call the {@link PluginHandler#unload(Plugin)} method.
	 */
	public void unload();
}

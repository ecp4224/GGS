/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.mcforge.API.ManualLoad;
import net.mcforge.server.Server;

public class PluginHandler {
	private ArrayList<Plugin> plugins = new ArrayList<Plugin>();

	private ArrayList<Game> games = new ArrayList<Game>();

	private final ClassLoader loader = URLClassLoader.newInstance(new URL[]{}, getClass().getClassLoader());
	/**
	 * Unload a plugin from memory.
	 * @param p
	 *         The plugin to unload
	 */
	public void unload(Plugin p) {
		if (plugins.contains(p)) {
			p.onUnload();
			plugins.remove(p);
		}
	}

	public void loadFile(Server server, File arg0) {
		JarFile file = null;
		try {
			file = new JarFile(arg0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (file != null) {
			Enumeration<JarEntry> entries = file.entries();
			addPath(arg0);
			if (entries != null) {
				while (entries.hasMoreElements()) {
					JarEntry fileName = entries.nextElement();
					if (fileName.getName().endsWith(".class")) {
						try {
							String fullName = fileName.getName();
							int lastSlash = fullName.lastIndexOf('/');
							String path = fullName.substring(0, lastSlash + 1);
							//System.out.println(fullName.length() + "-" + path.length() + "-" + ".class".length());
							String name = fullName.substring(path.length(), fullName.length() - ".class".length());

							Class<?> class_ = Class.forName(path.replace('/', '.') + name, true, loader);
							if (Plugin.class.isAssignableFrom(class_)) {
								Class<? extends Plugin> pluginClass = class_.asSubclass(Plugin.class);
								Constructor<? extends Plugin> constructByServer = null;
								Exception tmpEx = null;
								try {
									constructByServer = pluginClass.getConstructor(Server.class);
								} catch (Exception ex) {
									tmpEx = ex;
								}
								Constructor<? extends Plugin> constructByServerProperties = null;
								try {
									constructByServerProperties = pluginClass.getConstructor(Server.class, Properties.class);
								} catch (Exception ex) {
									if (tmpEx != null) {
										tmpEx.printStackTrace();
										ex.printStackTrace();
									}
								}

								JarEntry propFile = file.getJarEntry(path + name + ".config");
								Properties properties = new Properties();
								if (propFile != null) {
									try {
										properties.load(file.getInputStream(propFile));
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}

								Plugin plugin = null;
								if (constructByServerProperties != null) {
									try {
										plugin = constructByServerProperties.newInstance(server, properties);
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
								if (constructByServer != null && plugin == null) {
									try {
										plugin = constructByServer.newInstance(server);
									} catch (Exception ex) {
										ex.printStackTrace();
									}
									if(plugin != null) plugin.setProperties(properties);
								}
								if (plugin == null) {
									System.out.println("Plugin could not be load: " + name);
									continue;
								}
								if (plugin.getClass().getAnnotation(ManualLoad.class) != null) //Ignore manual loading plugins
									continue;
								loadPlugin(plugin, server);
							} else {
								if (!Command.class.isAssignableFrom(class_)) {
									continue;
								}
								try {
									Class<? extends Command> commandClass = class_.asSubclass(Command.class);
									Constructor<? extends Command> construct = commandClass.getConstructor();
									Command c = construct.newInstance();
									if (c.getClass().getAnnotation(ManualLoad.class) != null) //Ignore manual loading commands
										continue;
									server.getCommandHandler().addCommand(c);
									CommandLoadEvent cle = new CommandLoadEvent(c, server);
									server.getEventSystem().callEvent(cle);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
							}

						} catch (ClassNotFoundException ex) {
							ex.printStackTrace();
						} 
					}
				}
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void loadPlugin(Plugin plugin, Server server) {
		plugin.onLoad(new String[]{"-normal"});
		if (plugin instanceof Game) {
			games.add((Game) plugin);
			server.Add((Game) plugin);
		} else {
			plugins.add(plugin);
		}
		PluginLoadEvent ple = new PluginLoadEvent(plugin, server);
		server.getEventSystem().callEvent(ple);
	}

	public void loadplugins(Server server) {
		File pluginFolder = new File("plugins/");
		if (!pluginFolder.exists()) {
			pluginFolder.mkdir();
			return;
		}
		File[] pluginFiles = pluginFolder.listFiles();
		for (int i = 0; i < pluginFiles.length; i++) {
			if (pluginFiles[i].isFile() && pluginFiles[i].getName().endsWith(".jar")) {
				loadFile(server, pluginFiles[i]);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	private void addPath(File f) {
		try {
			URL u = f.toURL();
		Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] {URL.class });
		method.setAccessible(true);
		method.invoke(loader, u);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.API.plugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Scanner;

import com.gamezgalaxy.GGS.server.Server;

public class PluginHandler {
	private ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	
	public void loadplugins(Server server) {
		if (!new File("plugins/").exists())
			new File("plugins/").mkdir();
		File folder = new File("plugins/");
		File[] listOfFiles = folder.listFiles();
		final String[] args = new String[] { "-normal" };
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".config")) {
			  String classpath = getClass(listOfFiles[i]);
			  if (classpath.equals("null"))
				  continue;
			  try {
				loadPlugin(new File("plugins/" + classpath.split("=")[0].trim()), classpath.split("=")[1].trim(), args, server);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  } 
		}
	}
	private String getClass(File file) {
		try {
			Scanner scan = new Scanner(file);
			return scan.nextLine();
		} catch(Exception e) {
			return "null";
		}
	}
	private void loadPlugin(File file, String classpath, String[] args, Server server) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
		ClassLoader loader = URLClassLoader.newInstance(
				new URL[] { file.toURL() },
				getClass().getClassLoader()
				);
		Class<?> clazz = Class.forName(classpath, true, loader);
		Class<? extends Plugin> runClass = clazz.asSubclass(Plugin.class);
		// Avoid Class.newInstance, for it is evil.
		Constructor<? extends Plugin> ctor = runClass.getConstructor(Server.class);
		Plugin doRun = ctor.newInstance(server);
		plugins.add(doRun);
		doRun.onLoad(args);
	}

}

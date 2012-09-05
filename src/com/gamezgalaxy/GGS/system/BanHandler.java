/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.system;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import com.gamezgalaxy.GGS.util.FileUtils;

public class BanHandler {
	static ArrayList<String> banned = new ArrayList<String>();
	
	public static void init() {
		banned.clear();

		try {
			FileUtils.CreateIfNotExist(FileUtils.PROPS_DIR , FileUtils.BANNED_FILE);
			FileInputStream fstream = new FileInputStream(FileUtils.PROPS_DIR + FileUtils.BANNED_FILE);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line;
			while((line = br.readLine()) != null)
				banned.add(line);

			fstream.close();
			in.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void save() throws FileNotFoundException {
		FileUtils.DeleteIfExist(FileUtils.PROPS_DIR + FileUtils.BANNED_FILE);
		PrintWriter out = new PrintWriter(FileUtils.PROPS_DIR + FileUtils.BANNED_FILE);
		for (String s : banned) {
			out.println(s);
		}
		out.close();
	}
	
	public static String getExpire(String name) {
		for (String s : banned) {
			if (s.split("\\:")[0].equalsIgnoreCase(name))
				return s.split("\\:")[1];
		}
		return "";
	}
	
	public static void ban(String username, String expire) {
		banned.add(username + ":" + expire);
		try {
			save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void unban(String username) {
		
		for(int i=0; i<banned.size() ;i++)
		{
			String[] ban = banned.get(i).split(":");
			if(ban[0].equalsIgnoreCase(username))
			{
				banned.remove(i);
				break;
			}
		}
		
		try {
			save();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void ban(String username) {
		ban(username, "null");
	}
	
	public static void ban(String username, Date date) {
		ban(username, date.toString());
	}
	
	public static boolean pastDate(String username) {
		String date = getExpire(username);
		if (date.equals("null"))
			return false;
		Date now = new Date();
		@SuppressWarnings("deprecation")
		Date then = new Date(date);
		return then.before(now);
	}
	
	public static boolean isBanned(String name) {
		init();

		for (String s : banned) {
			if (s.split("\\:")[0].equalsIgnoreCase(name))
				return !pastDate(name);
		}
		return false;
	}

}

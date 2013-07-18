/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.util;

public abstract class Utils {
	/**
	 * Gets the possessive form of the specified string<br>
	 * Examples:<br>
	 * <code>
	 * getPossessiveForm("glass") returns <b>glass'</b>
	 * getPossessiveForm("apple") returns <b>apple's</b>
	 * </code>
	 * 
	 * @param noun - The noun to get the possessive form for
	 */
	public static String getPossessiveForm(String noun) {
		if (noun.isEmpty()) {
			return "";
		}
		String suffix = "";
		char last = noun.charAt(noun.length() - 1);
		if (last == ' ') {
			while(last == ' ') {
				last = noun.charAt(noun.length() - 1 - suffix.length());
			}
		}
		if (last == 's' || last == 'x') {
			return noun + "'" + suffix;
		}
		else {
			return noun + "'s" + suffix;
		}
	}
	
	/**
	* Gives back a specific part of the input, if it's too long to show in chat
	* 
	* @param input - The input lines
	* @param page - The page to show (starting at 1)
	* @return the page to show
	*/
	public static String[] getPage(String[] input, int page) {
		if (page < 1) {
			throw new IllegalArgumentException("The value of the \"page\" argument must be greater than one!");
		}
		--page;
		page*=7;
		int itemc = 0;
		if (input.length - page > 0) { itemc = input.length - page; }
		if (itemc > 7) { itemc = 7; }
		int ci = 0;
		String[] items = new String[7];
		for (int i=0;i<7;i++)
		{
			items[i] = input[page + ci];
			ci++;
		}
		return items;
	}
}

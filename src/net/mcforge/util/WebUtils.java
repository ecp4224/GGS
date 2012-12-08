/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
* An abstract class used for easier website interaction.
* Almost all of the methods in the class throw {@link IOException}
*/
public abstract class WebUtils {
	
    /**
     * Reads the contents of the website at the specified URL
     * 
     * @param filePath - The URL of the website to read from
     * 
     * @return A string list with the contents of the read website
     * @throws IOException If there's an error while reading from the website
     */
	public static List<String> readContentsToList(URL u) throws IOException {
		LineNumberReader reader = new LineNumberReader(new InputStreamReader(u.openStream()));
		List<String> lines = new ArrayList<String>();
		String line;
		while ((line = reader.readLine()) != null)
			lines.add(line);
		reader.close();
        return lines;
	}
	
    /**
     * Reads the contents of the website at the specified URL
     * 
     * @param filePath - The URL of the website to read from
     * 
     * @return A string array with the contents of the read website
     * @throws IOException If there's an error while reading from the website
     */
	public static String[] readContentsToArray(URL u) throws IOException {
        List<String> lines = readContentsToList(u);
        return lines.toArray(new String[lines.size()]);
	}
}

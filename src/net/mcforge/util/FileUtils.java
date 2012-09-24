/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

/**
 * The Class FileUtils.
 */
public class FileUtils {

	/**
	 * The directory for the log files
	 */
	public static final String LOG_DIR = "logs" + File.separator;

	/**
	 * The directory for the properties file
	 */
	public static final String PROPS_DIR = "properties" + File.separator;

	/**
	 * The file name for the banned file
	 */
	public static final String BANNED_FILE = "banned.txt";

	private static Scanner scanner;
	private static Formatter formatter;
	/**
	 * Creates the directory/file if it doesn't exist.
	 * 
	 * @param dir
	 *            the dir
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void createIfNotExist(String path, String fileName, String contents) throws IOException {
		File filePath = new File(path);
		File fileFile = new File(path, fileName);

		filePath.mkdirs();

		if (!fileFile.exists()) {
			fileFile.createNewFile();
			
			PrintWriter writer = new PrintWriter(fileFile);
			writer.write(contents);
			
			try {
				writer.close();
				writer.flush();
			}
			finally {
				writer = null;
			}
		}
	}
	
	/**
	 * Creates a file if it does not exists. if the file does exists,
	 * then nothing happens
	 * @param path
	 *            The directory of the file
	 * @param fileName
	 *                The file name
	 * @throws IOException
	 *                    This is thrown if there's a problem writing the file
	 */
	public static void createIfNotExist(String path, String fileName) throws IOException {
		createIfNotExist(path, fileName, "");
	}

	/**
	 * Creates a file if it does not exists. if the file does exists,
	 * then nothing happens
	 * @param fileName
	 *                The file name
	 * @throws IOException
	 *                    This is thrown if there's a problem writing the file
	 */
	public static void createIfNotExist(String fileName) throws IOException {
		File file = new File(fileName);
		if (!file.exists()) {
			file.createNewFile();
		}
	}

	/**
	 * Delete the file/directory if exist.
	 * 
	 * @param element
	 *              the element
	 */
	public static void deleteIfExist(String element) {
		File file = new File(element);
		if (file.exists()) {
			file.delete();
		}
	}
	public static void appendText(String filePath, String text, boolean newLine) throws IOException {
		formatter = new Formatter(new File(filePath)); //TODO: add a good jdocs for these 
		formatter.out().append(String.format("%s%s", text, newLine ? "\n" : ""));
		formatter.close();
	}
	public static void appendText(String filePath, String text) throws IOException {
		appendText(filePath, text, false);
	}
	public static String[] readAllLines(String filePath) throws IOException {
		scanner = new Scanner(new File(filePath));
		List<String> lines = new ArrayList<String>();
		while (scanner.hasNext()) {
			lines.add(scanner.nextLine());
		}
		return lines.toArray(new String[lines.size()]);
	}
	public static boolean exists(String filePath) {
		return new File(filePath).exists();
	}

}

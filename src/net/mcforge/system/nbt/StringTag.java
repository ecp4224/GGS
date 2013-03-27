/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.system.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The String tag is used to store strings. Its ID is 8.
 */
public class StringTag extends Tag<String> {
	@Override
	public byte getID() {
		return TagID.STRING.getID();
	}
	
	@Override
	public String getReadable() {
		return "TAG_String(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): '" + value + "'";
	}
	
	public StringTag(String tagName) {
		super(tagName);
	}
	
	public StringTag(String tagName, String value) {
		super(tagName, value);
	}
	
	public void write(DataOutputStream writer) throws IOException {
		writer.writeUTF(value);
	}
	
	public void read(DataInputStream reader) throws IOException {
		value = reader.readUTF();
	}
}
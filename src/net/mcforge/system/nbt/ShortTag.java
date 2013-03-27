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
 * The Short tag is used to store shorts. Its ID is 2.
 */
public class ShortTag extends Tag<Short> {
	@Override
	public byte getID() {
		return TagID.SHORT.getID();
	}
	
	@Override
	public String getReadable() {
		return "TAG_Short(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): " + value;
	}
	
	public ShortTag(String tagName) {
		super(tagName);
	}
	
	public ShortTag(String tagName, Short value) {
		super(tagName, value);
	}

	@Override
	public void write(DataOutputStream writer) throws IOException {
		writer.writeShort(value);
	}

	@Override
	public void read(DataInputStream reader) throws IOException {
		value = reader.readShort();
	}
}

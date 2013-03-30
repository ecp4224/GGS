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
 * The Long tag is used to store longs. Its ID is 4.
 */
public class LongTag extends Tag<Long> {
	@Override
	public byte getID() {
		return TagID.LONG.getID();
	}
	
	@Override
	public String getReadable() {
		return "TAG_Long(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): " + value;
	}
	
	public LongTag(String tagName) {
		super(tagName);
	}
	
	public LongTag(String tagName, Long value) {
		super(tagName, value);
	}

	@Override
	public void write(DataOutputStream writer) throws IOException {
		writer.writeLong(value);
	}

	@Override
	public void read(DataInputStream reader) throws IOException {
		value = reader.readLong();
	}
}

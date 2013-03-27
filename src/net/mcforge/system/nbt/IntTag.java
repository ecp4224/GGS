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
 * The Int tag is used to store integers. Its ID is 3.
 */
public class IntTag extends Tag<Integer> {
	@Override
	public byte getID() {
		return TagID.INT.getID();
	}
	
	@Override
	public String getReadable() {
		return "TAG_Int(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): " + value;
	}
	
	public IntTag(String tagName) {
		super(tagName);
	}
	
	public IntTag(String tagName, Integer value) {
		super(tagName, value);
	}

	@Override
	public void write(DataOutputStream writer) throws IOException {
		writer.writeInt(value);
	}

	@Override
	public void read(DataInputStream reader) throws IOException {
		value = reader.readInt();
	}
}

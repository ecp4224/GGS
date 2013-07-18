/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.system.nbt;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The Byte tag is used to store bytes. Its ID is 1.
 */
public class ByteTag extends Tag<Byte> {
	@Override
	public byte getID() {
		return TagID.BYTE.getID();
	}
	
	@Override
	public String getReadable() {
		return "TAG_Byte(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): " + value;
	}
	
	public ByteTag(String tagName) {
		super(tagName);
	}
	
	public ByteTag(String tagName, Byte value) {
		super(tagName, value);
	}

	@Override
	public void write(DataOutputStream writer) throws IOException {
		writer.writeByte(value);
	}

	@Override
	public void read(DataInputStream reader) throws IOException {
		value = reader.readByte();
	}
}

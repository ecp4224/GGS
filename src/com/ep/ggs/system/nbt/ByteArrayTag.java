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
 * The ByteArray tag is used to store arrays of bytes. Its ID is 11.
 */
public class ByteArrayTag extends Tag<byte[]>{
	@Override
	public byte getID() {
		return TagID.BYTE_ARRAY.getID();
	}

	@Override
	public String getReadable() {
		String name = "TAG_ByteArray(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): " + value.length + " entries\r\n{\r\n";
		String valueString = " ";
		
		for (int i = 0; i < value.length; i++) { 
			
			valueString += value[i];
			
			valueString += (i != (value.length - 1)) ? ", " : "\r\n";
		}
		valueString += "}";
		
		return name + valueString;
	}
	
	public ByteArrayTag(String tagName) {
		super(tagName);
	}
	
	public ByteArrayTag(String tagName, byte[] value) {
		super(tagName, value);
	}

	@Override
	public void write(DataOutputStream writer) throws IOException {
		writer.writeInt(value.length);
		writer.write(value);
	}

	@Override
	public void read(DataInputStream reader) throws IOException {
		value = new byte[reader.readInt()];
		reader.readFully(value);
	}
}

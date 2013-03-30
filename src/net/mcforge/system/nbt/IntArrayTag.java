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
 * The IntArray tag is used to store arrays of integers. Its ID is 11.
 */
public class IntArrayTag extends Tag<Integer[]> {
	@Override
	public byte getID() {
		return TagID.INT_ARRAY.getID();
	}

	@Override
	public String getReadable() {
		String name = "TAG_IntArray(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): " + value.length + " entries\r\n{\r\n";
		String valueString = "";
		
		for (int i = 0; i < value.length; i++) { 
			valueString += " " + value[i];
			
			valueString += (i != (value.length - 1)) ? ", " : "\r\n";
		}
		valueString += "}";
		
		return name + valueString;
	}
	
	public IntArrayTag(String tagName) {
		super(tagName);
	}

	public IntArrayTag(String tagName, Integer[] value) {
		super(tagName, value);
	}

	@Override
	public void write(DataOutputStream writer) throws IOException {
		writer.writeInt(value.length);
		for (int i = 0; i < value.length; i++) {
			writer.writeInt(value[i]);
		}
	}

	@Override
	public void read(DataInputStream reader) throws IOException {
		value = new Integer[reader.readInt()];
		for (int i = 0; i < value.length; i++) {
			value[i] = reader.readInt();
		}
	}

}

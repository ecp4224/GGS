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
 * The Float tag is used to store floats. Its ID is 5.
 */
public class FloatTag extends Tag<Float> {
	@Override
	public byte getID() {
		return TagID.FLOAT.getID();
	}
	
	@Override
	public String getReadable() {
		return "TAG_Float(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): " + value;
	}
	
	public FloatTag(String tagName) {
		super(tagName);
	}
	
	public FloatTag(String tagName, Float value) {
		super(tagName, value);
	}

	@Override
	public void write(DataOutputStream writer) throws IOException {
		writer.writeFloat(value);
	}

	@Override
	public void read(DataInputStream reader) throws IOException {
		value = reader.readFloat();
	}
}

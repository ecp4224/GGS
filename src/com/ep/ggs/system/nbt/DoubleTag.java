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
 * The Double tag is used to store strings. Its ID is 6.
 */
public class DoubleTag extends Tag<Double> {
	@Override
	public byte getID() {
		return TagID.DOUBLE.getID();
	}
	
	@Override
	public String getReadable() {
		return "TAG_Double(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): " + value;
	}
	
	public DoubleTag(String tagName) {
		super(tagName);
	}
	
	public DoubleTag(String tagName, Double value) {
		super(tagName, value);
	}

	@Override
	public void write(DataOutputStream writer) throws IOException {
		writer.writeDouble(value);
	}

	@Override
	public void read(DataInputStream reader) throws IOException {
		value = reader.readDouble();
	}
}

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The List tag is used to store lists of tags of the same type. Its ID is 9.
 */
public class ListTag extends Tag<List<Tag<?>>> {
	private byte tagID = -1;
	
	@Override
	public byte getID() {
		return TagID.LIST.getID();
	}

	@Override
	public String getReadable() {
		int size = value.size();
		String name = "TAG_List(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): " + size + " entries\r\n{\r\n";
		String valueString = "";
		
		for (int i = 0; i < size; i++) { 
			valueString += " " + value.get(i).getReadable() + "\r\n";
		}
		valueString += "}";
		
		return name + valueString;
	}
	
	public ListTag(String tagName) {
		super(tagName);
	}
	
	public ListTag(String tagName, List<Tag<?>> value) {
		super(tagName, value);
	}

	@Override
	public void write(DataOutputStream writer) throws IOException {
		int size = value.size();
		tagID = (size == 0) ? 1 : value.get(0).getID();
		
		writer.writeByte(tagID);
		writer.writeInt(size);
		
		for (int i = 0; i < size; i++) {
			Tag<?> tag = value.get(i);
			if (tag.getID() != tagID) {
				throw new RuntimeException("ListTag can only store tags of the same type!");
			}
			
			tag.write(writer);
		}
	}

	@Override
	public void read(DataInputStream reader) throws IOException {
		tagID = reader.readByte();
		int size = reader.readInt();
		value = new ArrayList<Tag<?>>();
		
		for (int i = 0; i < size; i++) {
			Tag<?> tag = Tag.getTag(tagID);
			tag.read(reader);
			value.add(tag);
		}
	}
	
	public void addTag(Tag<?> tag) {
		if (tagID != -1) {
			if (tag.getID() != tagID) {
				throw new IllegalArgumentException("This list doesn't support the specified tag!");
			}
		}
		else {
			tagID = tag.getID();
		}
		
		value.add(tag);
	}
	
	@Override
	public List<Tag<?>> getValue() {
		return Collections.unmodifiableList(value);
	}
	
	public synchronized void removeTag(Tag<?> tag) {
		if (value.contains(tag)) {
			value.remove(tag);
		}
	}
	
	public void clear() {
		value.clear();
	}
	
	public int getSize() {
		return value.size();
	}
}

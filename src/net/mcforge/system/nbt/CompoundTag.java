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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Compound tag is used to store other tags. Every NBT file must begin with a
 * compound tag. Its ID is 10.
 */
public class CompoundTag extends Tag<List<Tag<?>>> {
	@Override
	public byte getID() {
		return TagID.COMPOUND.getID();
	}

	@Override
	public String getReadable() {
		int size = value.size();
		String name = "TAG_Compound(" + ((tagName.equals("")) ? "None" : "'" + tagName + "'") + "): " + size + " entries\r\n{\r\n";
		String valueString = "";
		
		for (int i = 0; i < size; i++) { 
			valueString += " " + value.get(i).getReadable().replaceAll("\r\n", "\r\n ") + "\r\n";

		}
		valueString += "}";
		
		return name + valueString;
	}
	
	public CompoundTag(String tagName) {
		super(tagName);
	}
	
	public CompoundTag(String tagName, List<Tag<?>> value) {
		super(tagName, value);
	}
	
	@Override
	public void write(DataOutputStream writer) throws IOException {
		for (int i = 0; i < value.size(); i++) {
			Tag.writeTag(value.get(i), writer);
		}
		Tag.writeTag(new EndTag(), writer);
	}
	
	@Override
	public void read(DataInputStream reader) throws IOException {
		if (value != null) {
			value.clear();
		}
		else {
			value = new ArrayList<Tag<?>>();
		}
		Tag<?> tag;	
		while ((tag = Tag.readTag(reader)).getID() != TagID.END.getID()) {
			value.add(tag);
		}
	}
	
	@Override
	public List<Tag<?>> getValue() {
		return Collections.unmodifiableList(value);
	}
	
	public void addTag(Tag<?> tag) {
		if (value == null) {
			value = new ArrayList<Tag<?>>();
		}
		value.add(tag);
	}
	
	public synchronized void removeTag(Tag<?> tag) {
		if (value.contains(tag)) {
			value.remove(tag);
		}
	}
	
	public Tag<?> getTag(String tagName) {
		for (int i = 0; i < value.size(); i++) {
			Tag<?> tag = value.get(i);
			if (tag.tagName.equals(tagName)) {
				return tag;
			}
		}
		return null;
	}
	
	//TODO: set values by name and value
	//TODO: get values by name

}
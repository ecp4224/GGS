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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * The NBTFile class is used to handle NBT files. It contains methods that can
 * read and write to NBT files, either GZipped or not.
 */
public class NBTFile {
	private final String path;
	
	public NBTFile(String path) {
		this.path = path;
	}
	
	public void writeGZip(CompoundTag tag) throws IOException {
		DataOutputStream writer = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(path)));
		Tag.writeTag(tag, writer);
		writer.close();
	}
	
	public void write(CompoundTag tag) throws IOException {
		DataOutputStream writer = new DataOutputStream(new FileOutputStream(path));
		Tag.writeTag(tag, writer);
		writer.close();
	}
	
	public CompoundTag read() throws IOException {
		DataInputStream reader = new DataInputStream(new FileInputStream(path));
		
		Tag<?> tag = Tag.readTag(reader);
		
		if (!(tag instanceof CompoundTag)) {
			reader.close();
			throw new RuntimeException("NBT files must start with a compound tag!");
		}	
		reader.close();
		
		return (CompoundTag)tag;
	}
	
	public CompoundTag readGZip() throws IOException {
		DataInputStream reader = new DataInputStream(new GZIPInputStream(new FileInputStream(path)));
		
		Tag<?> tag = Tag.readTag(reader);
		
		if (!(tag instanceof CompoundTag)) {
			reader.close();
			throw new RuntimeException("NBT files must start with a compound tag!");
		}	
		reader.close();
		
		return (CompoundTag)tag;
	}

}

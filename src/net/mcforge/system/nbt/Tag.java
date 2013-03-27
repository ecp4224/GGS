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
 * The Tag class is the superclass for all of the NBT tag class. It contains
 * methods to get, read and write tags.
 */
public abstract class Tag<T> {
	protected String tagName;
	protected T value;
	
	public Tag(String tagName) {
		this.tagName = (tagName == null) ? "" : tagName;
	}
	
	public Tag(String tagName, T value) {
		this.tagName = (tagName == null) ? "" : tagName;
		this.value = value;
	}
	
	/**
	 * Returns the ID of the tag.
	 */
	public abstract byte getID();
	
	/**
	 * Gets the human readable format of the tag.
	 */
	public abstract String getReadable();

	/**
	 * Writes the tag value to the specified stream.<br>
	 * Use the {@link #writeTag(Tag, DataOutputStream)} method to write a tag
	 * correctly!
	 */
	public abstract void write(DataOutputStream writer) throws IOException;
	
	/**
	 * Reads the tag value from the specified stream.<br>
	 * Use the {@link #readTag(DataInputStream) method to read a tag correctly!
	 */
	public abstract void read(DataInputStream reader) throws IOException;

	/**
	 * Sets the value of the tag.
	 * 
	 * @param value
	 *            The new value of the tag.
	 */
	public void setValue(T value) {
		this.value = value;
	}
	
	/**
	 * Gets the value of the tag.
	 * 
	 * @return The value of the tag.
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * Gets the tag name of the specified tag.
	 * 
	 * @return The name of the tag.
	 */
	public String getName() {
		return tagName;
	}
	
	/**
	 * Sets the name of the specified tag.
	 * 
	 * @param tagName
	 *            The name to set.
	 */
	public void setName(String tagName) {
		this.tagName = (tagName == null) ? "" : tagName;
	}
	
	/**
	 * Gets an NBT tag by its tag ID.
	 * 
	 * @param ID
	 *            The ID of the tag to get.
	 */
	public static Tag<?> getTag(byte ID) {
		if (ID == TagID.END.getID()) {
			return new EndTag();
		}
		else if (ID == TagID.BYTE.getID()) {
			return new ByteTag("");
		}
		else if (ID == TagID.SHORT.getID()) {
			return new ShortTag("");
		}
		else if (ID == TagID.INT.getID()) {
			return new IntTag("");
		}
		else if (ID == TagID.LONG.getID()) {
			return new LongTag("");
		}
		else if (ID == TagID.FLOAT.getID()) {
			return new FloatTag("");
		}
		else if (ID == TagID.DOUBLE.getID()) {
			return new DoubleTag("");
		}
		else if (ID == TagID.BYTE_ARRAY.getID()) {
			return new ByteArrayTag("");
		}
		else if (ID == TagID.STRING.getID()) {
			return new StringTag("");
		}
		else if (ID == TagID.LIST.getID()) {
			return new ListTag("");
		}
		else if (ID == TagID.COMPOUND.getID()) {
			return new CompoundTag("");
		}
		else if (ID == TagID.INT_ARRAY.getID()) {
			return new IntArrayTag("");
		}
		else {
			throw new IllegalArgumentException("Unknow ID specified!");
		}
	}

	/**
	 * Reads the value of the specified tag using the specified stream and
	 * returns the tag.
	 * 
	 * @param reader
	 *            The DataInputStream to read from.
	 * @return The tag that was read.
	 */
	public static Tag<?> readTag(DataInputStream reader) throws IOException {
		Tag<?> t = getTag(reader.readByte());
		if (t.getID() != 0) {
			t.setName(reader.readUTF());
		}
		t.read(reader);
		
		return t;
	}
	
	/**
	 * Writes the specified tag to the specified stream.<br>
	 * The ID of the tag will be written too. If a tag other than {@link EndTag}
	 * is specified, the tag's name will also be written.
	 * 
	 * @param tag
	 *            The tag to write.
	 * @param writer
	 *            The DataOutputStream to write to.
	 */
	public static void writeTag(Tag<?> tag, DataOutputStream writer) throws IOException {
		writer.writeByte(tag.getID());
		if (tag.getID() != TagID.END.getID()) {
			writer.writeUTF(tag.getName());
			tag.write(writer);
		}
	}
	
	@Override
	public String toString() {
		return getReadable();
	}
	
	@Override
    public int hashCode()
    {
        return super.hashCode() ^ this.value.hashCode();
    }
}

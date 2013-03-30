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
	
	/**
	 * Adds the specified tag to the compound tag.
	 * 
	 * @param tag
	 *            The tag to add.
	 */
	public void addTag(Tag<?> tag) {
		if (value == null) {
			value = new ArrayList<Tag<?>>();
		}
		value.add(tag);
	}
	
	/**
	 * Removes the specified tag from the compound tag if it was added before.
	 * 
	 * @param tag
	 *            The tag to remove.
	 */
	public synchronized void removeTag(Tag<?> tag) {
		if (value.contains(tag)) {
			value.remove(tag);
		}
	}
	
	/**
	 * Gets the specified tag by it's {@link Tag#getName() tag name}.
	 * 
	 * @param tagName
	 *            The name of the tag to get.
	 * 
	 * @return The tag if found, otherwise null.
	 */
	public Tag<?> getTag(String tagName) {
		for (int i = 0; i < value.size(); i++) {
			Tag<?> tag = value.get(i);
			if (tag.tagName.equals(tagName)) {
				return tag;
			}
		}
		return null;
	}
	
	private Object getObjectValue(String tagName) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return null;
		}
		
		return tag.getValue();
	}
	
	/**
	 * Gets a byte array from the tag with the specified tagName.<br>
	 * If the specified tag isn't in the compound tag null will be returned. A
	 * {@link RuntimeException} will be thrown if the specified tag isn't a byte
	 * array tag.
	 * 
	 * @param tagName
	 *            The name of the tag to get the value from.
	 * 
	 * @returns The value if the tag exists.
	 */
	public byte[] getByteArray(String tagName) {
		Object value = getObjectValue(tagName);
		
		if (value == null) {
			return null;
		}
		
		if (!(value instanceof byte[])) {
			throw new RuntimeException("The specified tag doesn't contain a byte array!");
		}
		
		return (byte[]) value;
	}
	
	
	public void setByteArray(String tagName, byte[] value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof ByteArrayTag)) {
			throw new RuntimeException("The specified tag isn't a byte array tag!");
		}
		
		((ByteArrayTag)tag).setValue(value);
		
	}
	
	/**
	 * Gets a byte from the tag with the specified tagName.<br>
	 * If the specified tag isn't in the compound tag null will be returned. A
	 * {@link RuntimeException} will be thrown if the specified tag isn't a byte
	 * tag.
	 * 
	 * @param tagName
	 *            The name of the tag to get the value from.
	 * 
	 * @returns The value if the tag exists.
	 */
	public byte getByte(String tagName) {
		Object value = getObjectValue(tagName);
		
		if (value == null) {
			return -1;
		}
		
		if (!(value instanceof Byte)) {
			throw new RuntimeException("The specified tag doesn't contain a byte!");
		}
		
		return ((Byte)value).byteValue();
	}
	
	public void setByteArray(String tagName, byte value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof ByteTag)) {
			throw new RuntimeException("The specified tag isn't a byte tag!");
		}
		
		((ByteTag)tag).setValue(value);	
	}
	
	/**
	 * Gets a compound tag with the specified tagName.<br>
	 * If the specified tag isn't in the compound tag null will be returned. A
	 * {@link RuntimeException} will be thrown if the specified tag isn't a
	 * compound tag.
	 * 
	 * @param tagName
	 *            The name of the tag to get.
	 * 
	 * @returns The tag if the it exists.
	 */
	public CompoundTag getCompoundTag(String tagName) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return null;
		}
		
		if (!(tag instanceof CompoundTag)) {
			throw new RuntimeException("The specified tag isn't a compound tag!");
		}
		
		return (CompoundTag) value;
	}
		
	public void setCompoundTag(String tagName, List<Tag<?>> value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof CompoundTag)) {
			throw new RuntimeException("The specified tag isn't a compound tag!");
		}
		
		((CompoundTag)tag).setValue(value);
	}
	
	/**
	 * Gets a double from the tag with the specified tagName.<br>
	 * If the specified tag isn't in the compound tag null will be returned. A
	 * {@link RuntimeException} will be thrown if the specified tag isn't a
	 * double tag.
	 * 
	 * @param tagName
	 *            The name of the tag to get the value from.
	 * 
	 * @returns The value if the tag exists.
	 */
	public double getDouble(String tagName) {
		Object value = getObjectValue(tagName);
		
		if (value == null) {
			return -1d;
		}
		
		if (!(value instanceof Double)) {
			throw new RuntimeException("The specified tag doesn't contain a double!");
		}
		
		return (Double)value;
	}
	
	public void setDouble(String tagName, double value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof DoubleTag)) {
			throw new RuntimeException("The specified tag isn't a double tag!");
		}
		
		((DoubleTag)tag).setValue(value);		
	}
	
	/**
	 * Gets a float from the tag with the specified tagName.<br>
	 * If the specified tag isn't in the compound tag null will be returned. A
	 * {@link RuntimeException} will be thrown if the specified tag isn't a
	 * float tag.
	 * 
	 * @param tagName
	 *            The name of the tag to get the value from.
	 * 
	 * @returns The value if the tag exists.
	 */
	public float getFloat(String tagName) {
		Object value = getObjectValue(tagName);
		
		if (value == null) {
			return -1F;
		}
		
		if (!(value instanceof Float)) {
			throw new RuntimeException("The specified tag doesn't contain a float!");
		}
		
		return (Float)value;
	}
	
	public void setFloat(String tagName, float value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof FloatTag)) {
			throw new RuntimeException("The specified tag isn't a float tag!");
		}
		
		((FloatTag)tag).setValue(value);	
	}
	
	/**
	 * Gets a int array from the tag with the specified tagName.<br>
	 * If the specified tag isn't in the compound tag null will be returned. A
	 * {@link RuntimeException} will be thrown if the specified tag isn't an int
	 * array tag.
	 * 
	 * @param tagName
	 *            The name of the tag to get the value from.
	 * 
	 * @returns The value if the tag exists.
	 */
	public int[] getIntArray(String tagName) {
		Object value = getObjectValue(tagName);
		
		if (value == null) {
			return null;
		}
		
		if (!(value instanceof Integer[])) {
			throw new RuntimeException("The specified tag doesn't contain an int array!");
		}
		
		return (int[]) value;
	}
	
	public void setIntArray(String tagName, Integer[] value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof IntArrayTag)) {
			throw new RuntimeException("The specified tag isn't an int array tag!");
		}
		
		((IntArrayTag)tag).setValue(value);	
	}
	
	/**
	 * Gets an int from the tag with the specified tagName.<br>
	 * If the specified tag isn't in the compound tag null will be returned. A
	 * {@link RuntimeException} will be thrown if the specified tag isn't an int
	 * tag.
	 * 
	 * @param tagName
	 *            The name of the tag to get the value from.
	 * 
	 * @returns The value if the tag exists.
	 */
	public int getInt(String tagName) {
		Object value = getObjectValue(tagName);
		
		if (value == null) {
			return -1;
		}
		
		if (!(value instanceof Integer)) {
			throw new RuntimeException("The specified tag doesn't contain an int!");
		}
		
		return (Byte)value;
	}
	
	public void setInt(String tagName, int value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof IntTag)) {
			throw new RuntimeException("The specified tag isn't an int tag!");
		}
		
		((IntTag)tag).setValue(value);		
	}
	
	/**
	 * Gets a {@link ListTag list tag} with the specified tagName.<br>
	 * If the specified tag isn't in the compound tag null will be returned. A
	 * {@link RuntimeException} will be thrown if the specified tag isn't a list
	 * tag.
	 * 
	 * @param tagName
	 *            The name of the tag to get.
	 * 
	 * @returns The list tag if it exists.
	 */
	public ListTag getListTag(String tagName) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return null;
		}
		
		if (!(tag instanceof ListTag)) {
			throw new RuntimeException("The specified tag isn't a list tag!");
		}
		
		return (ListTag) value;
	}
	
	public void setListTag(String tagName, List<Tag<?>> value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof ListTag)) {
			throw new RuntimeException("The specified tag isn't a list tag!");
		}
		
		((ListTag)tag).setValue(value);		
	}
	
	/**
	 * Gets a byte array from the tag with the specified tagName.<br>
	 * If the specified tag isn't in the compound tag null will be returned. A
	 * {@link RuntimeException} will be thrown if the specified tag isn't a byte
	 * array tag.
	 * 
	 * @param tagName
	 *            The name of the tag to get the value from.
	 * 
	 * @returns The value if the tag exists.
	 */
	public long getLong(String tagName) {
		Object value = getObjectValue(tagName);
		
		if (value == null) {
			return -1;
		}
		
		if (!(value instanceof Long)) {
			throw new RuntimeException("The specified tag doesn't contain a long!");
		}
		
		return (Long) value;
	}
	
	public void setLong(String tagName, long value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof LongTag)) {
			throw new RuntimeException("The specified tag isn't a long tag!");
		}
		
		((LongTag)tag).setValue(value);		
	}
	
	public short getShort(String tagName) {
		Object value = getObjectValue(tagName);
		
		if (value == null) {
			return -1;
		}
		
		if (!(value instanceof Short)) {
			throw new RuntimeException("The specified tag doesn't contain a short!");
		}
		
		return (Short) value;
	}
	
	public void setShort(String tagName, short value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof ShortTag)) {
			throw new RuntimeException("The specified tag isn't a short tag!");
		}
		
		((ShortTag)tag).setValue(value);		
	}
	
	public String getString(String tagName) {
		Object value = getObjectValue(tagName);
		
		if (value == null) {
			return null;
		}
		
		if (!(value instanceof String)) {
			throw new RuntimeException("The specified tag doesn't contain a string!");
		}
		
		return (String) value;
	}
	
	public void setString(String tagName, String value) {
		Tag<?> tag = getTag(tagName);
		
		if (tag == null) {
			return;
		}
		
		if (!(tag instanceof StringTag)) {
			throw new RuntimeException("The specified tag isn't a string tag!");
		}
		
		((StringTag)tag).setValue(value);		
	}
}
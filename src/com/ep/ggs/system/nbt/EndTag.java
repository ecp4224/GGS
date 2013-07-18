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
 * The End tag represents the end of a compound. It cannot be named and it
 * doesn't have a value. Its ID is 0.
 */
public class EndTag extends Tag<Object> {
	@Override
	public byte getID() {
		return TagID.END.getID();
	}
	
	@Override
	public String getReadable() {
		return "SWAG";
	}
	
	public EndTag() {
		super(null);
	}

	@Override
	public void write(DataOutputStream writer) throws IOException {
	}

	@Override
	public void read(DataInputStream reader) throws IOException {
	}
}

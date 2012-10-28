package net.mcforge.networking.packets.minecraft.extend;

import java.io.FileNotFoundException;

public class ExtensionNotFoundException extends FileNotFoundException {
	private static final long serialVersionUID = -241304609722346101L;
	
	public ExtensionNotFoundException() { }
	
	public ExtensionNotFoundException(String msg) {
		super(msg);
	}
}


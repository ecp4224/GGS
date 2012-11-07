package net.mcforge.networking.packets.minecraft.extend;

import java.io.FileNotFoundException;

import net.mcforge.API.ClassicExtension;

public class ExtensionNotSupportedException extends FileNotFoundException {
	private static final long serialVersionUID = -241304609722346101L;
	
	public ExtensionNotSupportedException() { }
	
	public ExtensionNotSupportedException(String msg) {
		super(msg);
	}
	
	public ExtensionNotSupportedException(ClassicExtension c) {
		this("The extension " + c.extName() + " is not supported by the client!");
	}
	
	public ExtensionNotSupportedException(Class<?> class_) {
		this(getExtension(class_));
	}
	
	public ExtensionNotSupportedException(Object o) {
		this(o.getClass());
	}
	
	private static ClassicExtension getExtension(Class<?> class_) {
		ClassicExtension c = null;
		if ((c = class_.getAnnotation(ClassicExtension.class)) != null)
			return c;
		return null;
	}
}

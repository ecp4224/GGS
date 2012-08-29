package com.gamezgalaxy.GGS.chat;

public class ColorFormatException extends IllegalArgumentException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1268801460803261859L;
	public ColorFormatException(String paramString)
	{
		super(paramString);
	}
	public static ColorFormatException forInputString(String paramString)
	{
		return new ColorFormatException("For input string: \"" + paramString + "\"");
	}
}

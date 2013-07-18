/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.chat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ChatColor {
    Black('&', '0', "Black"),
    Dark_Blue('&', '1', "Navy"),
    Dark_Green('&','2', "Green"),
    Dark_Aqua('&','3', "Teal"),
    Dark_Red('&','4', "Maroon"),
    Purple('&','5', "Purple"),
    Orange('&','6', "Gold"),
    Gray('&', '7', "Silver"),
    Dark_Gray('&','8', "Gray"),
    Indigo('&','9', "Blue"),
    Bright_Green('&','a', "Lime"),
    Aqua('&', 'b', "Aqua"),
    Red('&', 'c', "Red"),
    Pink('&', 'd', "Pink"),
    Yellow('&', 'e', "Yellow"),
    White('&', 'f', "White");
    
    private char id;
    private char type;
    private String mcName;
    ChatColor(char id, char type, String mcName) { 
    	this.id = id; 
    	this.type = type; 
    	this.mcName = mcName;
    	}
    
    public char getColor() {
        return type;
    }
    public char getID() {
        return id;
    }
    /**
     * Gets the color's minecraft name
     */
    public String getName() {
    	return mcName;
    }
    @Override
    public String toString() {
        return "" + id + type;
    }
    
    /**
     * Converts a message containing unformatted color codes to formatted
     * 
     * @param message
     * @return string
     */
    public static String convertColorCodes(String message)
    {
        String m = message;
        if(!m.matches(".*%([0-9]|[a-f]|[k-r])%([0-9]|[a-f]|[k-r])%([0-9]|[a-f]|[k-r])")){
            if(m.matches(".*%([0-9]|[a-f]|[k-r])(.+?).*")){
                Pattern pattern = Pattern.compile("%([0-9]|[a-f]|[k-r])(.+?)");
                Matcher matcher = pattern.matcher(m);
                while (matcher.find()) {
                    String code = matcher.group().substring(1);
                    m = m.replaceAll("%"+code, "&"+code);
                }
            }
        }
        return m;
    }
    
    /**
     * Parse the String <b>color</b> to a ChatColor object
     * @param color The String to parse
     * @return The ChatColor object that equals the String
     */
    public static ChatColor parse(String color) {
    	if (color == null) {
    		return null;
    	}
        return parse(color.toCharArray()[(color.length() == 1) ? 0 : 1]);
    }
    
    /**
     * Parse the char <b>color</b> to a ChatColor object.
     * @param color The char to parse
     * @return The ChatColor object that equals the char
     */
    public static ChatColor parse(char color) {
        if (color == '0')
            return ChatColor.Black;
        else if (color == '1')
            return ChatColor.Dark_Blue;
        else if (color == '2')
            return ChatColor.Dark_Green;
        else if (color == '3')
            return ChatColor.Dark_Aqua;
        else if (color == '4')
            return ChatColor.Dark_Red;
        else if (color == '5')
            return ChatColor.Purple;
        else if (color == '6')
            return ChatColor.Orange;
        else if (color == '7')
            return ChatColor.Gray;
        else if (color == '8')
            return ChatColor.Dark_Gray;
        else if (color == '9')
            return ChatColor.Indigo;
        else if (color == 'a')
            return ChatColor.Bright_Green;
        else if (color == 'b')
            return ChatColor.Aqua;
        else if (color == 'c')
            return ChatColor.Red;
        else if (color == 'd')
            return ChatColor.Pink;
        else if (color == 'e')
            return ChatColor.Yellow;
        else if (color == 'f')
            return ChatColor.White;
        throw ColorFormatException.forInputString("" + color);
    }
    
    /**
     * Gets a ChatColor by it's Minecraft name
     * 
     * @param color The char to parse
     * @return The ChatColor object that equals the char
     */
    public static ChatColor fromName(String name) {
        if (name.equalsIgnoreCase("Black"))
            return ChatColor.Black;
        else if (name.equalsIgnoreCase("Navy"))
            return ChatColor.Dark_Blue;
        else if (name.equalsIgnoreCase("Green"))
            return ChatColor.Dark_Green;
        else if (name.equalsIgnoreCase("Teal"))
            return ChatColor.Dark_Aqua;
        else if (name.equalsIgnoreCase("Maroon"))
            return ChatColor.Dark_Red;
        else if (name.equalsIgnoreCase("Purple"))
            return ChatColor.Purple;
        else if (name.equalsIgnoreCase("Gold"))
            return ChatColor.Orange;
        else if (name.equalsIgnoreCase("Silver"))
            return ChatColor.Gray;
        else if (name.equalsIgnoreCase("Gray"))
            return ChatColor.Dark_Gray;
        else if (name.equalsIgnoreCase("Blue"))
            return ChatColor.Indigo;
        else if (name.equalsIgnoreCase("Lime"))
            return ChatColor.Bright_Green;
        else if (name.equalsIgnoreCase("Aqua"))
            return ChatColor.Aqua;
        else if (name.equalsIgnoreCase("Red"))
            return ChatColor.Red;
        else if (name.equalsIgnoreCase("Pink"))
            return ChatColor.Pink;
        else if (name.equalsIgnoreCase("Yellow"))
            return ChatColor.Yellow;
        else if (name.equalsIgnoreCase("White"))
            return ChatColor.White;
        throw ColorFormatException.forInputString(name);
    }
}


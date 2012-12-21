package net.mcforge.networking.packets.minecraft;

import java.io.UnsupportedEncodingException;

public class PacketHelper {
    
    public static String arrayToString(byte[] source, int index, short size) {
        byte[] array = new byte[size * 2];
        for (int i = index; i <= (size * 2) + 2; i++) {
            array[i - index] = source[i];
        }
        String string = "UNDEFINED";
        try {
            string = new String(array, 0, array.length, "UTF-16BE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

}

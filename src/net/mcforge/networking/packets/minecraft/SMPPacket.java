/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.DynamicPacket;
import net.mcforge.networking.packets.InvalidPacketCall;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.clients.Client;
import net.mcforge.server.Server;

public abstract class SMPPacket extends DynamicPacket {

    public SMPPacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
        addClientSupport(Client.MINECRAFT);
    }
    
    /**
     * Read a string from the {@link DataInputStream} object.
     * @param input
     *             The input to read from
     * @return
     *        A string
     * @throws IOException
     */
    public String readString(DataInputStream input) throws IOException {
        short len = input.readShort();
        byte[] array = new byte[len * 2];
        input.read(array, 0, len * 2);
        String toreturn = new String(array, 0, len * 2, "UTF-16BE");
        return toreturn;
    }
    
    /**
     * Put a string in a byte array in the format of UTF-16BE
     * @param string
     *              The string to put into the array
     * @param array
     *             The array to modify
     * @param start
     *             The starting index
     */
    public void putString(String string, byte[] array, int start) {
        short len = (short)string.length();
        try {
            byte[] sarray = string.getBytes("UTF-16BE");
            ByteBuffer b = ByteBuffer.allocate(2 + sarray.length);
            b.putShort(len);
            b.put(sarray);
            byte[] output = b.array();
            b = null;
            for (int i = start; i < output.length; i++)
                array[i] = output[i - start];
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    
    /**
     * Put a minecraft string into a ByteBuffer
     * @param string
     *              The string to put into the ByteBuffer
     * @param buffer
     *              The buffer to insert into
     */
    public void putMinecraftString(String string, ByteBuffer buffer) {
        try {
            short len = (short)string.length();
            byte[] sarray = string.getBytes("UTF-16BE");
            buffer.putShort(len);
            buffer.put(sarray);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
    
	/**
	 * Gets the byte length of a Minecraft string.
	 * 
	 * @param string
	 *            The string to get the length for.
	 */
    public int stringLength(Object string) {
    	if (!(string instanceof String)) {
    		throw new IllegalArgumentException("Object isn't instanceof string!");
    	}
    	
    	return ((String)string).length() * 2;
    }
    
    /**
     * Put a boolean into a ByteBuffer
     * @param bool
     *              The boolean to put into the ByteBuffer
     * @param buffer
     *              The buffer to insert into
     */
    public void putBoolean(boolean bool, ByteBuffer buffer) {
    	buffer.put((byte)(bool ? 0x01 : 0x00));
    }
    
    @Override
    public void handle(Server server, IOClient client, InputStream reader) {
        if (client instanceof SMPPlayer) {
            DataInputStream da = new DataInputStream(reader);
            handle((SMPPlayer)client, server, da);
        }
        else
            throw new InvalidPacketCall("This packet cant handle a SMPPacket with a non SMP Player");
    }
    
    @Override
    public void Write(IOClient client, Server server) {
        Write(client, server, new Object[0]);
    }
    
    @Override
    public void Write(IOClient client, Server server, Object... obj) {
        if (client instanceof SMPPlayer)
            write((SMPPlayer)client, server, obj);
        else
            throw new InvalidPacketCall("You cant send a SMPPacket to a non SMP Player");
    }
    
    public abstract void write(SMPPlayer client, Server server, Object... obj);
    
    public abstract void handle(SMPPlayer client, Server server, DataInputStream reader);
    
    @Override
    public void write(Server server, IOClient player, OutputStream writer, Object... obj) {
        Write(player, server, obj);
    }

}

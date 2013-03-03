package net.mcforge.networking.packets;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import net.mcforge.networking.IOClient;
import net.mcforge.server.Server;

public abstract class DynamicPacket extends Packet {

    public DynamicPacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public abstract void handle(Server server, IOClient player, InputStream reader);
    
    public abstract void write(Server server, IOClient player, OutputStream writer, Object... obj);
    

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
        throw new InvalidPacketCall("This is a dynamic packet, you can not provide it a byte-array.");
    }

    @Override
    public void Write(IOClient client, Server servers) {
        write(servers, client, client.getOutputStream());
    }
    
    @Override
    public void Write(IOClient client, Server server, Object... obj) {
        write(server, client, client.getOutputStream(), obj);
    }
    
    @Override
    public boolean dynamicSize() {
        return true;
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
		short len = (short) string.length();
		try {
			byte[] sarray = string.getBytes("UTF-16BE");
			ByteBuffer b = ByteBuffer.allocate(2 + sarray.length);
			b.putShort(len);
			b.put(sarray);
			byte[] output = b.array();
			b = null;
			for (int i = start; i < output.length; i++)
				array[i] = output[i - start];
		}
		catch (Exception e) {
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
			short len = (short) string.length();
			byte[] sarray = string.getBytes("UTF-16BE");
			buffer.putShort(len);
			buffer.put(sarray);
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}

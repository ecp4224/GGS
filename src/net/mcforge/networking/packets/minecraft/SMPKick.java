package net.mcforge.networking.packets.minecraft;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.DynamicPacket;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class SMPKick extends DynamicPacket {

    public SMPKick(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public SMPKick(PacketManager parent) {
        this("SMPKick", (byte)0xFF, parent);
    }

    @Override
    public void handle(Server server, IOClient player, InputStream reader) {
        
    }

    @Override
    public void write(Server server, IOClient player, OutputStream writer,
            Object... obj) {
        try {
            String reason = obj.length == 0 ? "No reason given" : (String)obj[0];
            byte[] array = reason.getBytes("UTF-16BE");
            ByteBuffer b = ByteBuffer.allocate(3 + array.length);
            b.put(ID);
            b.putShort((short)reason.length());
            b.put(array);
            writer.write(b.array());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

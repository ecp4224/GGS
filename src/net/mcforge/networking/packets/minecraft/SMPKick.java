package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class SMPKick extends SMPPacket {

    public SMPKick(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public SMPKick(PacketManager parent) {
        this("SMPKick", (byte)0xFF, parent);
    }

    @Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
        
    }

    @Override
    public void write(SMPPlayer client, Server server, Object... obj) {
        try {
            String reason = obj.length == 0 ? "No reason given" : (String)obj[0];
            byte[] array = reason.getBytes("UTF-16BE");
            ByteBuffer b = ByteBuffer.allocate(3 + array.length);
            b.put(ID);
            b.putShort((short)reason.length());
            b.put(array);
            client.writeData(b.array());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

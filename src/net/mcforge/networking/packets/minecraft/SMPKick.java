package net.mcforge.networking.packets.minecraft;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class SMPKick extends Packet {

    public SMPKick(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public SMPKick(PacketManager parent) {
        this("SMPKick", (byte)0xFF, parent);
    }
    
    @Override
    public boolean dynamicSize() {
        return true;
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) { }

    @Override
    public void Write(IOClient client, Server servers, Object... obj) {
        String reason = (String)obj[0];
        try {
            byte[] array = reason.getBytes("UTF-16BE");
            ByteBuffer b = ByteBuffer.allocate(3 + array.length);
            b.put(ID);
            b.putShort((short)reason.length());
            b.put(array);
            client.writeData(b.array());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Write(IOClient client, Server servers) {
       Write(client, servers, "No reason given");
    }

}

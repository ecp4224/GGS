package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class EncryptionResponse extends SMPPacket {

    public EncryptionResponse(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public EncryptionResponse(PacketManager pm) {
        this("ER", (byte)0xFC, pm);
    }

    @Override
    public void write(SMPPlayer client, Server server, Object... obj) {
        ByteBuffer b = ByteBuffer.allocate(5);
        b.put(ID);
        b.putShort((short)0);
        b.put(new byte[0]);
        b.putShort((short)0);
        b.put(new byte[0]);
        try {
            client.writeData(b.array());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(SMPPlayer client, Server server, DataInputStream reader) {
        try {
            short ssl = reader.readShort();
            byte[] secret = new byte[ssl];
            reader.read(secret);
            short vl = reader.readShort();
            byte[] verify = new byte[vl];
            reader.read(verify);
            client.validateLogin(secret, verify, this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}

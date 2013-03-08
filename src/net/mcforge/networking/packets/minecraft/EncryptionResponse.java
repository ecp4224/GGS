package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
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
            client.validateLogin(secret, verify);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

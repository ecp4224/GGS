package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.DynamicPacket;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class Handshake extends DynamicPacket {

    public Handshake(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public Handshake(PacketManager pm) {
        super("Hankshake", (byte)0x02, pm);
    }

    @Override
    public void handle(Server server, IOClient player, InputStream reader) {
        try {
            DataInputStream bais = new DataInputStream(reader);
            byte ver = bais.readByte();
            String username = readString(bais);
            String server_host = readString(bais);
            int port = bais.readInt();
            System.out.println(ver + " : " + username + " : " + server_host + " : " + port);
            if (player instanceof SMPPlayer) {
                ((SMPPlayer)player).username = username;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(Server server, IOClient player, OutputStream writer, Object... obj) { }

}

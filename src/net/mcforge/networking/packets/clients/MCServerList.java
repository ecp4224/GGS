package net.mcforge.networking.packets.clients;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.IClient;
import net.mcforge.networking.packets.PacketManager;

public class MCServerList implements IClient {

    @Override
    public byte getOPCode() {
        return (byte)0xFE;
    }

    @Override
    public IOClient create(Socket client, PacketManager pm) {
        IOClient ic = new IOClient(client, pm);
        DataOutputStream dos = new DataOutputStream(ic.getOutputStream());
        try {
            dos.write((byte)0xFF);
            String data = "§1\0\51\0\1.4.7\0" + pm.server.MOTD + "\0" + pm.server.getSMPPlayers().size() + "\0" + pm.server.MaxPlayers;
            dos.writeShort((short)data.length());
            dos.writeChars(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        
    }

}

package net.mcforge.networking.packets;

import java.net.Socket;

import net.mcforge.networking.IOClient;

public interface IClient {
    public byte getOPCode();
    
    public IOClient create(Socket client, PacketManager pm);
}

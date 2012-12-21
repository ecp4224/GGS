package net.mcforge.iomodel;

import java.net.Socket;

import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;

public class SMPPlayer extends IOClient {

    public SMPPlayer(Socket client, PacketManager pm) {
        super(client, pm);
    }
    
    public void kick(String reason) {
        Packet p = pm.getPacket("SMPKick");
        p.Write(this, pm.server, reason);
        //closeConnection();
    }

}

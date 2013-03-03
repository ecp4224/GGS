package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class TimeUpdate extends SMPPacket {

    public TimeUpdate(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public TimeUpdate(PacketManager pm) {
        this("LoginRequest", (byte)0x04, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 2) {
            ByteBuffer bb;
            
            if (obj[0] instanceof Long && obj[1] instanceof Long) {
                bb = ByteBuffer.allocate(17);
                
                bb.put(ID);
                bb.putLong((Long)obj[0]);
                bb.putLong((Long)obj[1]);
                
                try {
                    player.writeData(bb.array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

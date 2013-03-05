package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class AttachEntity extends SMPPacket {

    public AttachEntity(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public AttachEntity(PacketManager pm) {
        this("AttachEntity", (byte)0x27, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 2) {
            ByteBuffer bb;
            
            if (obj[0] instanceof Integer && obj[1] instanceof Integer) {
                bb = ByteBuffer.allocate(9);
                
                bb.put(ID);
                bb.putInt((Integer)obj[0]);
                bb.putInt((Integer)obj[1]);

				try {
					player.writeData(bb.array());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
    }
}

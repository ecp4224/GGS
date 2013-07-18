package com.ep.ggs.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.ep.ggs.iomodel.SMPPlayer;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;


public class Particle extends SMPPacket {

    public Particle(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public Particle(PacketManager pm) {
        this("Particle", (byte)0x3F, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 9) {
            ByteBuffer bb;
            
            if (obj[0] instanceof String && obj[1] instanceof Float && obj[2] instanceof Float &&
                obj[3] instanceof Float && obj[4] instanceof Float && obj[5] instanceof Float && 
                obj[6] instanceof Float && obj[7] instanceof Float && obj[8] instanceof Integer) {
                bb = ByteBuffer.allocate(34 + ((String)obj[0]).length() * 2);
                
                bb.put(ID);
                putMinecraftString((String)obj[0], bb);
                bb.putFloat((Float)obj[1]);
                bb.putFloat((Float)obj[2]);
                bb.putFloat((Float)obj[3]);
                bb.putFloat((Float)obj[4]);
                bb.putFloat((Float)obj[5]);
                bb.putFloat((Float)obj[6]);
                bb.putFloat((Float)obj[7]);
                bb.putInt((Integer)obj[8]);

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

package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class Entity extends SMPPacket {

    public Entity(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public Entity(PacketManager pm) {
        this("Entity", (byte)0x1E, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 1) {
            ByteBuffer bb;
            if (obj[0] instanceof Integer) {
                bb = ByteBuffer.allocate(5);
                
                bb.put(ID);
                bb.putInt((Integer)obj[0]);
                
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

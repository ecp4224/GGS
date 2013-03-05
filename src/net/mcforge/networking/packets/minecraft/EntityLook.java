package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class EntityLook extends SMPPacket {

    public EntityLook(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public EntityLook(PacketManager pm) {
        this("EntityLook", (byte)0x20, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 3) {
            ByteBuffer bb;
            
            if (obj[0] instanceof Integer && obj[1] instanceof Byte && obj[2] instanceof Byte) {
                bb = ByteBuffer.allocate(7);
                
                bb.put(ID);
                bb.putInt((Integer)obj[0]);
                bb.put((Byte)obj[1]);
                bb.put((Byte)obj[2]);

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

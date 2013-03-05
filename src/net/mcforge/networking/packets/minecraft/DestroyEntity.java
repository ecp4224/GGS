package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class DestroyEntity extends SMPPacket {

    public DestroyEntity(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public DestroyEntity(PacketManager pm) {
        this("DestroyEntity", (byte)0x1D, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 4) {
            ByteBuffer bb;
            if (obj[0] instanceof Byte && obj[1] instanceof Integer[]) {
                
                Integer[] array = (Integer[])obj[1];
                bb = ByteBuffer.allocate(2 + array.length * 4);
                
                bb.put(ID);
                bb.put((Byte)obj[0]);              
                for (int i = 0; i < array.length; i++) {
                	bb.putInt(array[i]);
                }
                
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

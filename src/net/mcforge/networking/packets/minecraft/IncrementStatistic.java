package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class IncrementStatistic extends SMPPacket {

    public IncrementStatistic(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public IncrementStatistic(PacketManager pm) {
        this("IncrementStatistic", (byte)0xC8, pm);
    }

	@Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
	}

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >=  2) {
        	if (obj[0] instanceof Integer && obj[1] instanceof Byte) {
                ByteBuffer bb = ByteBuffer.allocate(6);
                
                bb.putInt((Integer)obj[0]);
                bb.put((Byte)obj[1]);
                
				try {
					p.writeData(bb.array());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }
}

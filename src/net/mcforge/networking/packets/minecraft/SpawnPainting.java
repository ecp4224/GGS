package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class SpawnPainting extends SMPPacket {

    public SpawnPainting(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public SpawnPainting(PacketManager pm) {
        this("SpawnPainting", (byte)0x19, pm);
    }

    @Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 6 && obj[0] instanceof Integer && obj[1] instanceof String && obj[2] instanceof Integer &&
        	obj[3] instanceof Integer && obj[4] instanceof Integer && obj[5] instanceof Integer) {       
			ByteBuffer bb = ByteBuffer.allocate(23 + ((String) obj[1]).length() * 2);
			bb.put(this.ID);
			bb.putInt((Integer)obj[0]);
			putMinecraftString((String)obj[1], bb);
			bb.putInt((Integer)obj[2]);
			bb.putInt((Integer)obj[3]);
			bb.putInt((Integer)obj[4]);
			bb.putInt((Integer)obj[5]);
			try {
				p.writeData(bb.array());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}

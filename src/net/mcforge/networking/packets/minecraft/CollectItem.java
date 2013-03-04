package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class CollectItem extends SMPPacket {

    public CollectItem(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public CollectItem(PacketManager pm) {
        this("CollectItem", (byte)0x16, pm);
    }

    @Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 2 && obj[0] instanceof Integer && obj[1] instanceof Integer) {       
			ByteBuffer bb = ByteBuffer.allocate(9);
			bb.put(this.ID);
			bb.putInt((Integer)obj[0]);
			bb.putInt((Integer)obj[1]);
			try {
				p.writeData(bb.array());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}

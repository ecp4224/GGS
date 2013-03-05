package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class ItemData extends SMPPacket {

    public ItemData(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public ItemData(PacketManager pm) {
        this("ItemData", (byte)0x83, pm);
    }

	@Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
	}

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >=  4) {
        	if (obj[0] instanceof Short && obj[1] instanceof Short && obj[2] instanceof Short &&
        		obj[3] instanceof Byte[]) {
                ByteBuffer bb = ByteBuffer.allocate(7 + ((Byte[])obj[3]).length);
                
                bb.putShort((Short)obj[0]);
                bb.putShort((Short)obj[1]);
                bb.putShort((Short)obj[2]);
                bb.put((byte[])obj[3]);
                
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

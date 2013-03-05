package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class TabComplete extends SMPPacket {

    public TabComplete(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public TabComplete(PacketManager pm) {
        this("TabComplete", (byte)0xCB, pm);
    }

    @SuppressWarnings("unused") //temp
	@Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
		try {
			String input = readString(reader);
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 1) {
        	if (obj[0] instanceof String) {
				ByteBuffer bb = ByteBuffer.allocate(3 + ((String)obj[0]).length());
				bb.put(this.ID);
				putMinecraftString((String)obj[0], bb);
				
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

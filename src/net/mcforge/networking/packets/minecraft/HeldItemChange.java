package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class HeldItemChange extends SMPPacket {

    public HeldItemChange(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public HeldItemChange(PacketManager pm) {
        this("HeldItemChange", (byte)0x10, pm);
    }

    @Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
        try {
			player.setHeldItemSlot(reader.readShort());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 1 && obj[0] instanceof Short) {
            int ID = (Short)obj[0];
            if (ID < 0 || ID > 8) {
            	return;
            }
            
			ByteBuffer bb = ByteBuffer.allocate(3);
			bb.put(this.ID);
			bb.putInt(ID);
			try {
				p.writeData(bb.array());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
}

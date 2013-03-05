package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class EntityRelativeMove extends SMPPacket {

    public EntityRelativeMove(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public EntityRelativeMove(PacketManager pm) {
        this("EntityRelativeMove", (byte)0x1F, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 4) {
            ByteBuffer bb;
            if (obj[0] instanceof Integer && obj[1] instanceof Byte && obj[2] instanceof Byte && obj[3] instanceof Byte) {
                bb = ByteBuffer.allocate(8);
                
                bb.put(ID);
                bb.put((Byte)obj[0]);
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

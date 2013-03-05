package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class NamedSoundEffect extends SMPPacket {

    public NamedSoundEffect(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public NamedSoundEffect(PacketManager pm) {
        this("NamedSoundEffect", (byte)0x3E, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 6) {
            ByteBuffer bb;
            
            if (obj[0] instanceof String && obj[1] instanceof Integer && obj[2] instanceof Integer &&
                obj[3] instanceof Integer && obj[4] instanceof Float && obj[5] instanceof Byte) {
                bb = ByteBuffer.allocate(20 + ((String)obj[1]).length() * 2);
                
                bb.put(ID);
                putMinecraftString((String)obj[0], bb);
                bb.putInt((Integer)obj[1]);
                bb.putInt((Integer)obj[2]);
                bb.putInt((Integer)obj[3]);
                bb.putFloat((Float)obj[4]);
                bb.put((Byte)obj[5]);
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

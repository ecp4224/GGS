package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class UpdateScore extends SMPPacket {

    public UpdateScore(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public UpdateScore(PacketManager pm) {
        this("UpdateScore", (byte)0xCF, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 4) {
            ByteBuffer bb;
            
            if (obj[0] instanceof String && obj[1] instanceof Byte && obj[2] instanceof String &&
                obj[4] instanceof Integer) {
                bb = ByteBuffer.allocate(9 + stringLength(obj[0]) + stringLength(obj[2]));
                
                bb.put(ID);
                putMinecraftString((String)obj[0], bb);
                bb.put((Byte)obj[1]);
                putMinecraftString((String)obj[2], bb);
                bb.putInt((Integer)obj[3]);

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

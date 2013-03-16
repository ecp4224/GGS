package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class ScoreboardObjective extends SMPPacket {

    public ScoreboardObjective(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public ScoreboardObjective(PacketManager pm) {
        this("ScoreboardObjective", (byte)0xCE, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
        if (obj.length >= 3) {
            ByteBuffer bb;
            
            if (obj[0] instanceof String && obj[1] instanceof String && obj[2] instanceof Byte) {
                bb = ByteBuffer.allocate(6 + stringLength(obj[0]) + stringLength(obj[1]));
                
                bb.put(ID);
                putMinecraftString((String)obj[0], bb);
                putMinecraftString((String)obj[1], bb);
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

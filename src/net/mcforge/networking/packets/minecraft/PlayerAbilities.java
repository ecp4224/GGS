package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class PlayerAbilities extends SMPPacket {

    public PlayerAbilities(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public PlayerAbilities(PacketManager pm) {
        this("PlayerAbilities", (byte)0xCA, pm);
    }

    @SuppressWarnings("unused") //temp
	@Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
		try {
			byte bool = reader.readByte();
			boolean disableDamage = (bool & 1) > 0;
			boolean flying = (bool & 2) > 0;
			boolean allowFlying = (bool & 4) > 0;
			boolean creativeMode = (bool & 8) > 0;
			
			byte flySpeed = reader.readByte();
			byte walkSpeed = reader.readByte();
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 3) {
        	if (obj[0] instanceof Byte && obj[1] instanceof Byte && obj[2] instanceof Byte) {
				ByteBuffer bb = ByteBuffer.allocate(4);
				bb.put(this.ID);
				bb.put((Byte)obj[0]);
				bb.put((Byte)obj[1]);
				bb.put((Byte)obj[2]);
				
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

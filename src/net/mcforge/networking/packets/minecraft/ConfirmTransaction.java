package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class ConfirmTransaction extends SMPPacket {

    public ConfirmTransaction(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public ConfirmTransaction(PacketManager pm) {
        this("ConfirmTransaction", (byte)0x6A, pm);
    }

    @SuppressWarnings("unused") //temp
	@Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
		try {
			byte windowID = reader.readByte();
			short actionNumber = reader.readShort();
			boolean accepted = reader.readBoolean();
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 3) {
        	if (obj[0] instanceof Byte && obj[1] instanceof Short && obj[2] instanceof Boolean) {
                ByteBuffer bb = ByteBuffer.allocate(5);
                
                bb.put((Byte)obj[0]);
                bb.putShort((Short)obj[1]);
                putBoolean((Boolean)obj[2], bb);
                
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

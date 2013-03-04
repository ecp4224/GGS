package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class PlayerLook extends SMPPacket {

    public PlayerLook(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public PlayerLook(PacketManager pm) {
        super("PlayerLook", (byte)0x0C, pm);
    }

	@Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {        
    	try {
    		p.setOldRotation(p.getRotation());
        	p.getRotation().set(reader.readFloat(), reader.readFloat());
        	p.setOnGround(reader.readBoolean());
		}
		catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) { }

}

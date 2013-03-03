package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class UseEntity extends SMPPacket {

    public UseEntity(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public UseEntity(PacketManager pm) {
        super("UseEntity", (byte)0x07, pm);
    }

    @SuppressWarnings("unused") //Temp
	@Override
    public void handle(SMPPlayer p, Server server, DataInputStream dis) {
    	try {
			int user = dis.readInt();
			int target = dis.readInt();
			boolean mouse = dis.readBoolean();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) { }

}

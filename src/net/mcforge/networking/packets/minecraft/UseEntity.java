package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.DynamicPacket;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class UseEntity extends DynamicPacket {

    public UseEntity(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public UseEntity(PacketManager pm) {
        super("UseEntity", (byte)0x07, pm);
    }

    @SuppressWarnings("unused") //Temp
	@Override
    public void handle(Server server, IOClient player, InputStream reader) {
        SMPPlayer p;
        if (player instanceof SMPPlayer)
            p = (SMPPlayer)player;
        else {
            return;
        }
        
    	try {
        	DataInputStream dis = new DataInputStream(reader);
			int user = dis.readInt();
			int target = dis.readInt();
			boolean mouse = dis.readBoolean();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void write(Server server, IOClient player, OutputStream writer, Object... obj) { }

}

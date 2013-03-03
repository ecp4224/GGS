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

public class Player extends DynamicPacket {

    public Player(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public Player(PacketManager pm) {
        super("Player", (byte)0x0A, pm);
    }

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
        	boolean onGround = dis.readBoolean();
			p.setOnGround(onGround);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void write(Server server, IOClient player, OutputStream writer, Object... obj) { }

}

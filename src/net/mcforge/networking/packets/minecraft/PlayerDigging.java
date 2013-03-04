package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class PlayerDigging extends SMPPacket {

    public PlayerDigging(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public PlayerDigging(PacketManager pm) {
        super("PlayerDigging", (byte)0x0E, pm);
    }

	@SuppressWarnings("unused") //temp
	@Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {        
    	try {
    		byte status = reader.readByte();
    		int x = reader.readInt();
    		byte y = reader.readByte();
    		int z = reader.readInt();
    		byte face = reader.readByte();
    		
    		if ((Math.abs(x) <= Math.abs(p.getLocation().getX()) + 6) &&
    			(Math.abs(y) <= Math.abs(p.getLocation().getY()) + 6) &&
    			(Math.abs(z) <= Math.abs(p.getLocation().getZ()) + 6)) {
    			//only accept digging packets with coordinates within a 6-unit radius of the player's position. 
    		}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) { }

}

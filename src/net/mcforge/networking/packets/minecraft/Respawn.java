package net.mcforge.networking.packets.minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.DynamicPacket;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class Respawn extends DynamicPacket {

    public Respawn(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public Respawn(PacketManager pm) {
        this("Respawn", (byte)0x09, pm);
    }

    @Override
    public void handle(Server server, IOClient player, InputStream reader) {
    }

    @Override
    public void write(Server server, IOClient player, OutputStream writer, Object... obj) {
        Write(player, server, obj);
    }
    
    @Override
    public void Write(IOClient player, Server server, Object... obj) {
    	SMPPlayer p;
    	if (player instanceof SMPPlayer) {
    		p = (SMPPlayer)player;
    	}
    	else {
    		return;
    	}
    	
    	if (obj.length >= 5) {
    		ByteBuffer bb;
    		
    		if (obj[0] instanceof Integer && obj[1] instanceof Byte && obj[2] instanceof Byte &&
    			obj[3] instanceof Short && obj[4] instanceof String) {
    			bb = ByteBuffer.allocate(11 + ((String)obj[4]).length() * 2);
    			
    			bb.put(ID);
    			bb.putInt((Integer)obj[0]);
    			bb.put((Byte)obj[1]);
    			bb.put((Byte)obj[2]);
    			bb.putShort((Byte)obj[3]);
    			putMinecraftString((String)obj[4], bb);
        		
                try {
                    p.writeData(bb.array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
    		}
    	}
    }
}

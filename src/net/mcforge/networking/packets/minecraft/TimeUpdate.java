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

public class TimeUpdate extends DynamicPacket {

    public TimeUpdate(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public TimeUpdate(PacketManager pm) {
        this("LoginRequest", (byte)0x04, pm);
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
    	
    	if (obj.length >= 2) {
    		ByteBuffer bb;
    		
    		if (obj[0] instanceof Long && obj[1] instanceof Long) {
    			bb = ByteBuffer.allocate(17);
    			
    			bb.put(ID);
    			bb.putLong((Long)obj[0]);
    			bb.putLong((Long)obj[1]);
        		
                try {
                    p.writeData(bb.array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
    		}
    	}
    }
}

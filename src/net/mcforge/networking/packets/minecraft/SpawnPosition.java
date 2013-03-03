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

public class SpawnPosition extends DynamicPacket {

    public SpawnPosition(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public SpawnPosition(PacketManager pm) {
        this("SpawnPosition", (byte)0x06, pm);
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
    	
    	if (obj.length >= 3) {
    		ByteBuffer bb;
    		
    		if (obj[0] instanceof Integer && obj[1] instanceof Integer && obj[2] instanceof Integer) {
    			bb = ByteBuffer.allocate(13);
    			
    			bb.put(ID);
    			bb.putInt((Integer)obj[0]);
    			bb.putInt((Integer)obj[1]);
    			bb.putInt((Integer)obj[2]);
        		
                try {
                    p.writeData(bb.array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
    		}
    	}
    }
}

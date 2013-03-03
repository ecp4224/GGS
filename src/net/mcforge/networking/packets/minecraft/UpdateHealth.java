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

public class UpdateHealth extends DynamicPacket {

    public UpdateHealth(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public UpdateHealth(PacketManager pm) {
        this("UpdateHealth", (byte)0x08, pm);
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
    		
    		if (obj[0] instanceof Short && obj[1] instanceof Short && obj[2] instanceof Float) {
    			bb = ByteBuffer.allocate(8);
    			
    			bb.put(ID);
    			bb.putShort((Short)obj[0]);
    			bb.putShort((Short)obj[1]);
    			bb.putFloat((Float)obj[2]);
        		
                try {
                    p.writeData(bb.array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
    		}
    	}
    }
}

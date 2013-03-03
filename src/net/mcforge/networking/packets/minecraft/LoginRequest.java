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

public class LoginRequest extends DynamicPacket {

    public LoginRequest(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public LoginRequest(PacketManager pm) {
        this("LoginRequest", (byte)0x01, pm);
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
    	
    	if (obj.length >= 7) {
    		ByteBuffer bb;
    		
    		if (obj[0] instanceof Integer && obj[1] instanceof String && obj[2] instanceof Byte && 
    			obj[3] instanceof Byte && obj[4] instanceof Byte && obj[5] instanceof Byte && obj[6] instanceof Byte) {
    			bb = ByteBuffer.allocate(12 + ((String)obj[1]).length() * 2);
    			
    			bb.put(ID);
    			bb.putInt((Integer)obj[0]);
        		putMinecraftString((String)obj[1], bb);
        		bb.put((Byte)obj[2]);
        		bb.put((Byte)obj[3]);
        		bb.put((Byte)obj[4]);
        		bb.put((Byte)obj[5]);
        		bb.put((Byte)obj[6]);
        		
                try {
                    p.writeData(bb.array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
    		}
    	}
    }
}

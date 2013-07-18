package com.ep.ggs.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.ep.ggs.iomodel.SMPPlayer;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.networking.packets.PacketParamException;
import com.ep.ggs.server.Server;


public class Teams extends SMPPacket {

    public Teams(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public Teams(PacketManager pm) {
        this("Teams", (byte)0xD1, pm);
    }

    @Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {
    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) {
    	if (obj.length == 2) {
    		if (obj[0] instanceof String && obj[2] instanceof Byte) {
    			byte mode = (Byte)obj[1];
    			
    			if (mode != 1) {
    				throw new PacketParamException(this, obj, "Mode(obj[1]) can only be 1 for this number of values in the array!");
    			}
    			
    			ByteBuffer bb;
                
                if (obj[0] instanceof String && obj[1] instanceof Byte) {
                	
                    bb = ByteBuffer.allocate(4 + stringLength(obj[0]));
                    
                    bb.put(ID);
                    putMinecraftString((String)obj[0], bb);
                    bb.put(mode);
                    
    				try {
    					player.writeData(bb.array());
    				}
    				catch (IOException e) {
    					e.printStackTrace();
    				}
                }
    		}
    	}
    	else if (obj.length == 6) {
			byte mode = (Byte)obj[1];
			
			if (mode != 0 && mode != 2) {
				throw new PacketParamException(this, obj, "Mode(obj[1]) can only be 0 and 2 for this number of values in the array!");
			}
			
			ByteBuffer bb;
            
            if (obj[0] instanceof String && obj[1] instanceof Byte && obj[2] instanceof String &&             
            	obj[3] instanceof String && obj[4] instanceof String && obj[5] instanceof String) {
            	
                bb = ByteBuffer.allocate(11 + stringLength(obj[0]) + stringLength(obj[2]) + 
                		stringLength(obj[3]) + stringLength(obj[4]) + stringLength(obj[5]));
                
                bb.put(ID);
                putMinecraftString((String)obj[0], bb);
                bb.put(mode);
                putMinecraftString((String)obj[2], bb);
                putMinecraftString((String)obj[3], bb);
                putMinecraftString((String)obj[4], bb);
                putMinecraftString((String)obj[5], bb);
                
				try {
					player.writeData(bb.array());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
            }
    	}
    	
    	else if (obj.length >= 8) {
			byte mode = (Byte)obj[1];
			
			if (mode != 0 && mode != 3 && mode != 4) {
				throw new PacketParamException(this, obj, "Mode(obj[1]) can only be 0, 3 and 4 for this number of values in the array!");
			}
			
            ByteBuffer bb;
            
            if (obj[0] instanceof String && obj[1] instanceof Byte && obj[2] instanceof String &&             
            	obj[3] instanceof String && obj[4] instanceof String && obj[5] instanceof Byte && 
            	obj[6] instanceof Short && obj[7] instanceof String[]) {
            	
            	String[] str = (String[])obj[7];
            	int length = 0;
            	for (int i = 0; i < str.length; i++) {
            		length += 2 + stringLength(str[i]) * 2;
            	}
            	
                bb = ByteBuffer.allocate(12 + stringLength(obj[0]) + stringLength(obj[2]) + 
                		stringLength(obj[3]) + stringLength(obj[4]) + length);
                
                bb.put(ID);
                putMinecraftString((String)obj[0], bb);
                bb.put(mode);
                putMinecraftString((String)obj[2], bb);
                putMinecraftString((String)obj[3], bb);
                putMinecraftString((String)obj[4], bb);
                bb.put((Byte)obj[5]);
                bb.putShort((Short)obj[6]);
                
                for (int i = 0; i < str.length; i++) {
                	putMinecraftString(str[i], bb);
                }
                
				try {
					player.writeData(bb.array());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
            }
        }
    }
}

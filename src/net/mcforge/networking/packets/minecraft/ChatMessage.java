package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class ChatMessage extends SMPPacket {

    public ChatMessage(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public ChatMessage(PacketManager pm) {
        this("ChatMessage", (byte)0x03, pm);
    }

    @Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
        try {
            String message = readString(reader);
            
            if (message.startsWith("/")) {
            	player.performCommand(message.substring(1));
            	return;
            }
            
            player.getServer().broadcastMessage("<" + player.username + "> " + message);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 1) {
        	if (obj[0] instanceof String) {
				ByteBuffer bb = ByteBuffer.allocate(3 + stringLength(obj[0]));
				bb.put(this.ID);
				putMinecraftString((String)obj[0], bb);
				
				try {
					p.writeData(bb.array());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }
}

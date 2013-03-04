package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class PlayerPositionAndLook extends SMPPacket {

    public PlayerPositionAndLook(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public PlayerPositionAndLook(PacketManager pm) {
        this("PlayerPositionAndLook", (byte)0x0D, pm);
    }

    @Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
        try {
        	player.setOldLocation(player.getLocation());
        	player.setOldRotation(player.getRotation());
            double x = reader.readDouble();
            double y = reader.readDouble();
            double stance = reader.readDouble();
            double z = reader.readDouble();
            float yaw = reader.readFloat();
            float pitch = reader.readFloat();
            boolean onGround = reader.readBoolean();
            
            player.getLocation().set(x, y, z);
            player.setStance(stance);
            player.getRotation().setYaw(yaw); //TODO: make set like in Location
            player.getRotation().setPitch(pitch);
            player.setOnGround(onGround);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 7) {
        	if (obj[0] instanceof Double && obj[1] instanceof Double && obj[2] instanceof Double && 
        		obj[3] instanceof Double && obj[4] instanceof Float && obj[5] instanceof Float && obj[6] instanceof Boolean) {
        			ByteBuffer bb = ByteBuffer.allocate(42);
        			
        			bb.put(ID);
        			bb.putDouble((Double)obj[0]);
        			bb.putDouble((Double)obj[1]);
        			bb.putDouble((Double)obj[2]);
        			bb.putDouble((Double)obj[3]);
        			bb.putFloat((Float)obj[4]);
        			bb.putFloat((Float)obj[5]);
        			putBoolean((Boolean)obj[6], bb);
        		
                    try {
                        p.writeData(bb.array());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        	}
        }
    }
}

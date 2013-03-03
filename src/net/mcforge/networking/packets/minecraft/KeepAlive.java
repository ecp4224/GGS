package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class KeepAlive extends SMPPacket {

    public KeepAlive(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public KeepAlive(PacketManager pm) {
        this("KeepAlive", (byte)0x00, pm);
    }

    @Override
    public void handle(SMPPlayer player, Server server, DataInputStream reader) {
        int ID;
        try {
            ID = reader.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (ID == player.getPingID() || ID == 0)
            player.ping();
    }

    @Override
    public void write(SMPPlayer p, Server server, Object... obj) {
        if (obj.length >= 1 && obj[0] instanceof Integer) {
            int ID = (Integer)obj[0];
            ByteBuffer bb = ByteBuffer.allocate(5);
            bb.put(this.ID);
            bb.putInt(ID);
            try {
                p.writeData(bb.array());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

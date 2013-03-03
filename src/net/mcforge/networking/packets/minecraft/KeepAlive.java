package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.DynamicPacket;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class KeepAlive extends DynamicPacket {

    public KeepAlive(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    
    public KeepAlive(PacketManager pm) {
        this("KeepAlive", (byte)0x00, pm);
    }

    @Override
    public void handle(Server server, IOClient player, InputStream reader) {
        SMPPlayer p;
        if (player instanceof SMPPlayer)
            p = (SMPPlayer)player;
        else
            return;
        DataInputStream dis = new DataInputStream(reader);
        int ID;
        try {
            ID = dis.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (ID == p.getPingID() || ID == 0)
            p.ping();
    }

    @Override
    public void write(Server server, IOClient player, OutputStream writer, Object... obj) {
        Write(player, server, obj);
    }
    
    @Override
    public void Write(IOClient player, Server server, Object... obj) {
        SMPPlayer p;
        if (player instanceof SMPPlayer)
            p = (SMPPlayer)player;
        else
            return;
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

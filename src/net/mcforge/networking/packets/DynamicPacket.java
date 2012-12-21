package net.mcforge.networking.packets;

import java.io.InputStream;
import java.io.OutputStream;
import net.mcforge.networking.IOClient;
import net.mcforge.server.Server;

public abstract class DynamicPacket extends Packet {

    public DynamicPacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public abstract void handle(Server server, IOClient player, InputStream reader);
    
    public abstract void write(Server server, IOClient player, OutputStream writer);
    

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
        throw new InvalidPacketCall("This is a dynamic packet, you can not provide it a byte-array.");
    }

    @Override
    public void Write(IOClient client, Server servers) {
        throw new InvalidPacketCall("This is a dynamic packet, you must provide an OutputStreamWriter.");
    }
    
    @Override
    public boolean dynamicSize() {
        return true;
    }
}

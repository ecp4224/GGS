package net.mcforge.networking.packets.classicminecraft;

import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.clients.Client;

public abstract class ClassicPacket extends Packet {

    public ClassicPacket(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
        super.addClientSupport(Client.MINECRAFT_CLASSIC);
    }

}

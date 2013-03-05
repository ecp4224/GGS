package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class EnchantItem extends SMPPacket {

    public EnchantItem(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public EnchantItem(PacketManager pm) {
        super("EnchantItem", (byte)0x6C, pm);
    }

	@SuppressWarnings("unused") //temp
	@Override
    public void handle(SMPPlayer p, Server server, DataInputStream reader) {        
    	try {
    		byte windowID = reader.readByte();
    		byte enchantment = reader.readByte();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

    }

    @Override
    public void write(SMPPlayer player, Server server, Object... obj) { }
}

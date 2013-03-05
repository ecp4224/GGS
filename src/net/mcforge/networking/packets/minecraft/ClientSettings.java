package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class ClientSettings extends SMPPacket {

	public ClientSettings(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
	
	public ClientSettings(PacketManager pm) {
        this("ClientSettings", (byte)0xCC, pm);
    }
	
	
	@SuppressWarnings("unused")
	@Override
	public void handle(SMPPlayer client, Server server, DataInputStream reader) {
		try {
			//TODO: finish
			String locale = readString(reader);
			byte viewDistance = reader.readByte();
			byte chatFlags = reader.readByte();
			byte difficulty = reader.readByte();
			boolean showCape = reader.readBoolean();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(SMPPlayer client, Server server, Object... obj) { }
}

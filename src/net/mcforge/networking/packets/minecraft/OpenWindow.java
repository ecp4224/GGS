package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class OpenWindow extends SMPPacket {

	public OpenWindow(String name, byte ID, PacketManager parent) {
		super(name, ID, parent);
	}
	
	public OpenWindow(PacketManager pm) {
		this("OpenWindow", (byte)0x64, pm);
	}

	@Override
	public void handle(SMPPlayer client, Server server, DataInputStream reader) { }
	
	@Override
	public void write(SMPPlayer p, Server server, Object... obj) {
		if(obj.length >= 4 && obj[0] instanceof Byte && obj[1] instanceof Byte && obj[2] instanceof String && obj[3] instanceof Byte){
			ByteBuffer bb = ByteBuffer.allocate(6 + ((String)obj[2]).length() * 2);
			bb.put(this.ID);
			bb.put((Byte)obj[0]);
			bb.put((Byte)obj[1]);
			putMinecraftString((String)obj[1], bb);
			bb.put((Byte)obj[3]);
			
			try{
				p.writeData(bb.array());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

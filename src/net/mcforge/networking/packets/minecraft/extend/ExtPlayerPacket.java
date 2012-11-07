package net.mcforge.networking.packets.minecraft.extend;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.mcforge.API.ClassicExtension;
import net.mcforge.iomodel.Player;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.PacketType;
import net.mcforge.server.Server;

@ClassicExtension(extName = "ExtPlayer")
public class ExtPlayerPacket extends ExtendPacket {

	public ExtPlayerPacket(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
	}

	public ExtPlayerPacket(PacketManager packetManager) {
		this("ExtPlayer", (byte)0x39, packetManager, PacketType.Server_to_Client);
	}

	@Override
	public void WriteData(Player client, Server servers, Object... para) //Client is the spawner, para[0] is the other player
			throws ExtensionNotSupportedException {
		if (client.hasExtension(this)) {
			byte[] array = new byte[66];
			array[0] = ID;
			array[1] = ((Player)para[0]).getID();
			try {
				String cname = ((Player)para[0]).getClientName();
				while (cname.length() < 64)
					cname += " ";
				byte[] name = cname.getBytes("US-ASCII");
				System.arraycopy(name, 0, array, 2, name.length);
				client.WriteData(array);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			throw new ExtensionNotSupportedException(this);
		
	}

	@Override
	public void Handle(byte[] message, Server server, IOClient player) {
		// TODO Auto-generated method stub
		
	}

}

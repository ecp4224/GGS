package net.mcforge.networking.packets.minecraft.extend;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.mcforge.API.ClassicExtension;
import net.mcforge.iomodel.Player;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.PacketType;
import net.mcforge.server.Server;

@ClassicExtension(extName = "ExtAddPlayerName")
public class ExtAddPlayerNamePacket extends ExtendPacket {

	public ExtAddPlayerNamePacket(String name, byte ID, PacketManager parent,
			PacketType packetType) {
		super(name, ID, parent, packetType);
	}
	
	public ExtAddPlayerNamePacket(PacketManager parent) {
		this("ExtAddPlayerName", (byte)0x33, parent, PacketType.Server_to_Client);
	}

	@Override
	public void WriteData(Player client, Server servers, Object... para) //client is the player with the list to edit, para[0] is the player to add
			throws ExtensionNotSupportedException {
		if (client.hasExtension(this)) {
			Player toadd = (Player)para[0];
			byte[] array = new byte[195];
			array[0] = ID;
			array[1] = toadd.getID();
			try {
			String name = toadd.getName();
			byte[] bname = name.getBytes("US-ASCII");
			String disname = toadd.getDisplayName();
			byte[] bdisname = disname.getBytes("US-ASCII");
			String group = toadd.getGroup().name;
			byte[] bgroup = group.getBytes("US-ASCII");
			byte groupper = (byte)toadd.getGroup().permissionlevel; //I dont understand what is needed for...
				System.arraycopy(bname, 0, array, 2, bname.length);
				System.arraycopy(bdisname, 0, array, bname.length + 2, bdisname.length);
				System.arraycopy(bgroup, 0, array, bname.length + bdisname.length + 2, bgroup.length);
				array[194] = groupper;
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
	public void Handle(byte[] message, Server server, IOClient player) { }

}

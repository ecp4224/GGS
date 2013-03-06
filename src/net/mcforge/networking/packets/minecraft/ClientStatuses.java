package net.mcforge.networking.packets.minecraft;

import java.io.DataInputStream;
import java.io.IOException;

import net.mcforge.iomodel.SMPPlayer;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;

public class ClientStatuses extends SMPPacket {

	public ClientStatuses(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
	
	public ClientStatuses(PacketManager pm) {
        this("ClientStatuses", (byte)0xCD, pm);
	}
	
	@Override
	public void handle(SMPPlayer client, Server server, DataInputStream reader) {
		try {
			byte payload = reader.readByte();
			if (payload == 0) {
			    client.login();
			}
			else if (payload == 1) {
			    //TODO Respawn
			}
			else
			    client.kick("Invalid payload!");
		}
		catch(IOException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void write(SMPPlayer client, Server server, Object... obj) { }
}

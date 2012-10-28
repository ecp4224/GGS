/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.networking.packets.minecraft;

import java.io.*;

import net.mcforge.API.ClassicExtension;
import net.mcforge.networking.ClientType;

import net.mcforge.API.player.PlayerConnectEvent;
import net.mcforge.API.player.PlayerLoginEvent;
import net.mcforge.iomodel.Player;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.networking.packets.PacketType;
import net.mcforge.server.Server;
public class Connect extends Packet {

	public Connect(String name, byte ID, PacketManager parent, PacketType packetType) {
		super(name, ID, parent, packetType);
	}
	public Connect(PacketManager pm) {
		super("Player Connect", (byte)0x00, pm, PacketType.Client_to_Server);
		this.length = 130;
	}

	@Override
	public void Write(IOClient player, Server server) {
		// TODO Auto-generated method stub

	}
	@Override
	public void Handle(byte[] message, Server server, IOClient p) {
		Player player;
		if (p instanceof Player) {
			player = (Player)p;
		}
		else
			return;
		try {
			byte version = message[0];
			byte[] name = new byte[64];
			for (int i = 1; i < 64; i++)
				name[i - 1] = message[i];
			player.username = new String(name, "US-ASCII").trim();
			name = new byte[64];
			for (int i = 65; i < 65 + 32; i++)
				name[i - 65] = message[i];
			player.mppass = new String(name, "US-ASCII").trim();
			name = null;
			PlayerConnectEvent connect = new PlayerConnectEvent(player);
			server.getEventSystem().callEvent(connect);
			if (version != 0x07) {
				player.kick("Invalid protocol version!");
				return;
			}
			if (player.VerifyLogin() && !connect.isCancelled() && !connect.getAutologin()) {
				server.players.add(player);

				player.Login();
				player.client = ClientType.parse(message[129]);
				if (player.client == ClientType.Extend_Classic) {
					Packet packet = server.getPacketManager().getPacket((byte)0x10);
					packet.Write(player, player.getServer());
					packet = server.getPacketManager().getPacket((byte)0x11);
					for (ClassicExtension c : player.getServer().getPluginHandler().getExtensions()) {
						packet.Write(player, server, c);
					}
				}
				PlayerLoginEvent login = new PlayerLoginEvent(player);
				server.getEventSystem().callEvent(login);
			}
			else {
				if (!connect.getAutologin()) {
					if (connect.getKickMessage().equals(""))
						player.kick("Invalid Login!");
					else
						player.kick(connect.getKickMessage());
					return;
				}
				else
				{
					server.Log("plugin granted " + player.username + " verification bypass!");
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}


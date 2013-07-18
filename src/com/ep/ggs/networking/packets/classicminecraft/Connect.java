/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets.classicminecraft;

import java.io.UnsupportedEncodingException;

import com.ep.ggs.API.ClassicExtension;
import com.ep.ggs.API.player.PlayerConnectEvent;
import com.ep.ggs.API.player.PlayerLoginEvent;
import com.ep.ggs.iomodel.Player;
import com.ep.ggs.networking.ClassicClientType;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.Packet;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.server.Server;

public class Connect extends ClassicPacket {

    public Connect(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }
    public Connect(PacketManager pm) {
        super("Player Connect", (byte)0x00, pm);
        this.length = 130;
    }

    @Override
    public void Write(IOClient player, Server server) {
        
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
            if (server.getClassicLevelHandler() == null) {
                player.kick("Classic is not enabled on this server.");
                return;
            }
            if (player.verifyLogin() && !connect.isCancelled() && !connect.getAutologin()) {
                player.client = ClassicClientType.parse(message[129]);
                if (player.client == ClassicClientType.Extend_Classic) {
                    Packet packet = server.getPacketManager().getPacket("ExtInfo");
                    packet.Write(player, player.getServer());
                    packet = server.getPacketManager().getPacket("ExtEntry");
                    for (ClassicExtension c : player.getServer().getPluginHandler().getExtensions()) {
                        packet.Write(player, server, c);
                    }
                }
                player.login();
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


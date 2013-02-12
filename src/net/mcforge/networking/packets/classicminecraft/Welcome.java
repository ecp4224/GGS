/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.networking.packets.classicminecraft;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.mcforge.API.io.PacketPrepareEvent;
import net.mcforge.iomodel.Player;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;
import net.mcforge.world.model.ClassicLevel;

public class Welcome extends Packet {

    public Welcome(String name, byte ID, PacketManager parent) {
        super(name, ID, parent);
    }

    public Welcome(PacketManager pm) {
        super("Welcome", (byte)0x00, pm);
    }

    @Override
    public void Write(IOClient player, Server server) {
        PacketPrepareEvent event = new PacketPrepareEvent(player, this, server);
        server.getEventSystem().callEvent(event);
        if (event.isCancelled())
            return;
        try {
            boolean isop = false;
            if (player instanceof Player) {
                isop = ((Player)player).getGroup().isOP;
            }
            byte[] finals = new byte[131];
            finals[0] = ID;
            finals[1] = 0x07;
            String smotd = server.MOTD;
            if (!server.getSystemProperties().hasValue("use-textures")) {
                server.getSystemProperties().addSetting("use-textures", false);
                server.getSystemProperties().addComment("use-textures", "Whether or not to use WoM textures for XWoM and WoM users");
            }
            if (server.getSystemProperties().hasValue("use-textures") && server.getSystemProperties().getBool("use-textures") && ((Player)player).getLevel() == null)
                smotd = "&0cfg=" + Server.IP + ":" + server.Port + "/" + server.MainLevel;
            else if (((Player)player).getLevel() != null && !((Player)player).getLevel().getName().equals(server.MainLevel) && !((Player)player).getLevel().getName().equals("ignore") && ((Player)player).getLevel() instanceof ClassicLevel)
                smotd = ((ClassicLevel)((Player)player).getLevel()).motd;
            while (server.Name.length() < 64)
                server.Name += " ";
            while (smotd.length() < 64)
                smotd += " ";
            byte[] name = server.Name.getBytes("US-ASCII");
            byte[] motd = smotd.getBytes("US-ASCII");
            System.arraycopy(name, 0, finals, 2, name.length);
            System.arraycopy(motd, 0, finals, name.length + 2, motd.length);
            finals[130] = (isop ? (byte)100 : (byte)0);
            player.writeData(finals);
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Handle(byte[] message, Server server, IOClient player) {
    }

}


package net.mcforge.networking.packets.clients;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import net.mcforge.iomodel.Browser;
import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.IClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;

public class BrowserClient implements IClient {

    @Override
    public byte getOPCode() {
        return (byte)'G';
    }

    @Override
    public IOClient create(Socket client, PacketManager pm) {
        Browser b = new Browser(client, pm);
        b.setOutputStream(null);
        try {
            PrintStream writer = new PrintStream(client.getOutputStream(), false, "US-ASCII");
            b.setOutputStream(writer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Packet packet = pm.getPacket(getOPCode());
        if (packet == null)
            return null;
       else {
            byte[] message = new byte[0xFF];
            try {
                b.getInputStream().read(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (message.length < packet.length) {
                pm.server.Log("Bad packet..");
                return null;
            }
            else
                packet.Handle(message, pm.server, b);
            return b;
        }

    }
}

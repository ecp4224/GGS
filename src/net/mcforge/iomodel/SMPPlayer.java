package net.mcforge.iomodel;

import java.net.Socket;
import java.util.Random;

import net.mcforge.networking.IOClient;
import net.mcforge.networking.packets.Packet;
import net.mcforge.networking.packets.PacketManager;
import net.mcforge.server.Server;
import net.mcforge.system.ticker.Tick;

public class SMPPlayer extends IOClient implements Tick {
    
    private int pingID;
    private int dicpingc;
    private boolean pinged;
    public String username;
    
    private final static Random rand = new Random();

    public SMPPlayer(Socket client, PacketManager pm) {
        super(client, pm);
        pm.server.getTicker().addTick(this);
    }
    
    public void kick(String reason) {
        Packet p = pm.getPacket("SMPKick");
        p.Write(this, pm.server, reason);
    }
    
    public int getPingID() {
        return pingID;
    }
    
    public void ping() {
        pinged = true;
    }

    @Override
    public void tick() {
        if (!pinged) {
            dicpingc++;
            if (dicpingc > 4) {
                closeConnection();
                return;
            }
        }
        else
            dicpingc = 0;
        pinged = false;
        pingID = rand.nextInt();
        Packet p = pm.getPacket("KeepAlive");
        p.Write(this, getServer(), pingID);
    }
    
    public Server getServer() {
        return pm.server;
    }
    
    @Override
    public void closeConnection() {
        getServer().getTicker().removeTick(this);
        super.closeConnection();
    }

    @Override
    public boolean inSeperateThread() {
        return false;
    }

    @Override
    public int getTimeout() {
        return 60000 / 4;
    }

    @Override
    public String tickName() {
        return "SMPTick";
    }

}

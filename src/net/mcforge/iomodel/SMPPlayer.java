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
    private boolean onGround;
    
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
    
    /**
     * Whether the player is on ground.<br>
     * If the value is <b>True</b>, the player is either walking or swiming. If the value is <b>False</b>
     * the player is jumping or falling.<br>
     * This value is set by the {@link net.mcforge.networking.packets.minecraft.Player Player packet}.
     */
    public boolean isOnGround() {
    	return onGround;
    }
    
    /**
     * Controls if the player is marked as on ground.<br>
     * This value should only be set by the {@link net.mcforge.networking.packets.minecraft.Player Player packet}.
    */
    public void setOnGround(boolean onGround) {
    	this.onGround = onGround;
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

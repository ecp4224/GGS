package net.mcforge.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import net.mcforge.networking.packets.clients.Client;

public interface IOClient {
    
    public void listen();
    
    public long getReaderThreadID();
    
    public boolean isConnected();
    
    public InetAddress getInetAddress();
    
    public Client getClientType();
    
    public String getIP();
    
    public void closeConnection();
    
    public void writeData(byte[] data) throws IOException;
    
    public OutputStream getOutputStream();
    
    public void setOutputStream(OutputStream out);
    
    public InputStream getInputStream();
    
    public void setInputStream(InputStream in);
    
    public void setClienttype(Client clienttype);
}

/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import com.ep.ggs.iomodel.SimpleIOClient;
import com.ep.ggs.networking.IOClient;
import com.ep.ggs.networking.packets.PacketManager;
import com.ep.ggs.networking.packets.clients.Client;


public abstract class NetworkEntity extends Entity implements IOClient {
    
    private SimpleIOClient client;
    protected PacketManager pm;
    
    public NetworkEntity(Socket client, PacketManager pm) {
        super();
        this.pm = pm;
        this.client = new SimpleIOClient(client, pm);
    }
    
    
    public OutputStream getOutputStream() {
        return client.getOutputStream();
    }
    
    public void setOutputStream(OutputStream out) {
        client.setOutputStream(out);
    }
    
    public InputStream getInputStream() {
        return client.getInputStream();
    }
    
    public void setInputStream(InputStream in) {
        client.setInputStream(in);
    }
    
    public void writeData(byte[] data) throws IOException {
        client.writeData(data);
    }
    
    public void closeConnection() {
        client.closeConnection();
    }
    
    public void listen() {
        client.listen();
    }
    
    public SimpleIOClient getClient() {
        return client;
    }


    @Override
    public long getReaderThreadID() {
        return client.getReaderThreadID();
    }


    @Override
    public boolean isConnected() {
        return client.isConnected();
    }


    @Override
    public InetAddress getInetAddress() {
        return client.getInetAddress();
    }


    @Override
    public Client getClientType() {
        return client.getClientType();
    }


    @Override
    public String getIP() {
        return client.getIP();
    }


    @Override
    public void setClienttype(Client clienttype) {
        client.setClienttype(clienttype);
    }
}

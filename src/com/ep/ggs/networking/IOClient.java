/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import com.ep.ggs.networking.packets.clients.Client;


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

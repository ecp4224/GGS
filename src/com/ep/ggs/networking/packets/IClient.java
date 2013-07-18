/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.networking.packets;

import java.net.Socket;

import com.ep.ggs.networking.IOClient;


public interface IClient {
    public byte getOPCode();
    
    public IOClient create(Socket client, PacketManager pm);
}

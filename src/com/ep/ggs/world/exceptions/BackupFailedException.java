/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.world.exceptions;

import java.io.IOException;

/**
 * An exception object that is thrown when a level backup has failed.
 * @author MCForgeTeam
 */
public class BackupFailedException extends IOException {
    private static final long serialVersionUID = 1L;
    
    public BackupFailedException() {
        super();
    }
    
    public BackupFailedException(String message) {
        super(message);
    }

}

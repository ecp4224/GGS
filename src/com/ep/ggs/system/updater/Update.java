/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.system.updater;

public class Update {
    
    private int uid;
    private String version;
    private String download_url;
    private String minimum_core_version;
    private UpdateType type;
    private String update_type;
    
    private Update() { } //Hey hey hey STAY OUT OF MY SHED
    
    public int getUpdateID() {
        return uid;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getDownloadURL() {
        return download_url;
    }
    
    public String getCoreVersion() {
        return minimum_core_version;
    }
    
    public UpdateType getUpdateType() {
        if (type == null) {
            try {
                type = UpdateType.parse(update_type);
            } catch (Exception e) { }
        }
        return type;
    }

}

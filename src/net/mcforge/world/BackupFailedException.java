package net.mcforge.world;

import java.io.IOException;

public class BackupFailedException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public BackupFailedException() {
        super();
    }
    
    public BackupFailedException(String message) {
        super(message);
    }

}

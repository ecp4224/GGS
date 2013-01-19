package net.mcforge.world.exceptions;

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

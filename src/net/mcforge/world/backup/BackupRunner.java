package net.mcforge.world.backup;

import java.util.List;

import net.mcforge.server.Server;
import net.mcforge.world.Level;
import net.mcforge.world.exceptions.BackupFailedException;

public class BackupRunner extends Thread {
    private boolean runit;
    private boolean settingsloaded;
    private Server server;
    private int backup;
    private String location;
    
    public BackupRunner(Server server) {
        this.server = server;
    }
    
    private List<Level> getLevels() {
        return server.getLevelHandler().getLevelList();
    }
    
    /**
     * Start running the backup running. </br>
     * If the backup running is aready running then nothing will happen. </br>
     * If {@link BackupRunner#loadSettings()} was not called yet, it will be called before starting the backup.
     * To stop the BackupRunner, call {@link BackupRunner#stopRunning()}
     */
    public void startRunning() {
        if (runit)
            return;
        if (!settingsloaded)
            loadSettings();
        runit = true;
        start();
    }
    
    /**
     * Stop running the backup runner. </br>
     * This will cancel any pending backups and abort the thread as soon as possible. </br>
     * If a backup is currently in progress, then this method will block until that backup is complete.
     */
    public void stopRunning() {
        if (!runit)
            return;
        runit = false;
        try {
            interrupt();
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Load the backup settings from the server.
     * This method will be called from {@link BackupRunner#startRunning()} if it wasn't called already.
     */
    public void loadSettings() {
        if (!server.getSystemProperties().hasValue("backup-time")) {
            server.getSystemProperties().addSetting("backup-time", 300);
            server.getSystemProperties().addComment("backup-time", "The number of seconds between automatic backups");
        }
        backup = server.getSystemProperties().getInt("backup-time") * 1000;
        if (!server.getSystemProperties().hasValue("backup-location"))
            server.getSystemProperties().addSetting("backup-location", "backups");
        location = server.getSystemProperties().getValue("backup-location");
        settingsloaded = true;
    }

    @Override
    public void run() {
        while (runit) {
            for (int i = 0; i < getLevels().size(); i++) {
                if (interrupted())
                    break;
                try {
                    final Level l = getLevels().get(i);
                    if (!l.isAutoSaveEnabled())
                        continue;
                    l.backup(server, location);
                } catch (BackupFailedException e) {
                    server.logError(e);
                }
            }
            if (!runit)
                break;
            try {
                Thread.sleep(backup);
            } catch (InterruptedException e) { }
        }
    }

}

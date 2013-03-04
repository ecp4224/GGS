package net.mcforge.world.mcmodel;

import java.io.IOException;
import java.util.ArrayList;

import net.mcforge.server.Server;
import net.mcforge.system.ticker.Tick;
import net.mcforge.util.properties.Properties;
import net.mcforge.world.Level;
import net.mcforge.world.blocks.Block;
import net.mcforge.world.blocks.classicmodel.PhysicsBlock;
import net.mcforge.world.exceptions.BackupFailedException;
import net.mcforge.world.generator.Generator;

public class MCLevel implements Level {

    @Override
    public void generateWorld(Generator g) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAutoSaveEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasUpdated() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setAutoSave(boolean set) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void loadProperties() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Properties getLevelProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getTile(int x, int y, int z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Block getTile(int i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTile(Block b, int x, int y, int z, Server server) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void rawSetTile(int x, int y, int z, Block block, Server server,
            boolean addToTick) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setNewSpawn(int WaterLevel) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void save() throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unload(Server server) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unload(Server server, boolean save) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void backup(Server server, String location)
            throws BackupFailedException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void load(String filename, Server server) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public PhysicsBlock addTick(int x, int y, int z, Block b, Server server) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void startPhysics(Server server) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getLength() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getDepth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSpawnX() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSpawnY() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSpawnZ() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setWitdh(int width) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setHeight(int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDepth(int depth) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSpawnX(int spawnx) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSpawnY(int spawny) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setSpawnZ(int spawnz) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setName(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public long getVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ArrayList<Tick> getTicks() {
        // TODO Auto-generated method stub
        return null;
    }

}

package net.mcforge.world;

import java.io.File;
import java.util.List;

import net.mcforge.world.generator.classicmodel.ClassicGenerator;

public interface LevelHandler {
    public List<Level> getLevelList();
    
    public void generateLevel(String name, ClassicGenerator gen);
    
    public void generateLevel(String name, ClassicGenerator gen, Object... param);
    
    public Level findLevel(String name);
    
    public void loadLevels();
    
    public Level loadLevel(File file);
    
    public boolean unloadLevel(Level level);
    
    public void saveLevels();
    
    public void saveLevel(Level level);
}

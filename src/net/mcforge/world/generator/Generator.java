package net.mcforge.world.generator;

public interface Generator<E> {
    
    /**
     * The primary name of the generator
     * that can be specified by the player
     * when creating a new level
     */
    public String getName();
    /**
     * The other names for the level generator
     * that can be specified by the player
     * when creating a new world
     */
    public String[] getShortcuts();
    
    /**
     * Generate the object.
     * @param object
     */
    public void generate(E object);
}

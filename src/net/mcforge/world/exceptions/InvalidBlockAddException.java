package net.mcforge.world.exceptions;

import net.mcforge.world.blocks.Block;

public class InvalidBlockAddException extends IllegalAccessException {
    private static final long serialVersionUID = 2064347592399788357L;
    
    public InvalidBlockAddException(Block b) {
        super("The block " + b.name + " uses an ID that is already in use!");
    }
    
    public InvalidBlockAddException(Block b, String error) {
        super("The block " + b.name + " has caused an error: \"" + error + "\"");
    }

}

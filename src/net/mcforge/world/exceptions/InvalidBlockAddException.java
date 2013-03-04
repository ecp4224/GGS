package net.mcforge.world.exceptions;

import net.mcforge.world.blocks.classicmodel.ClassicBlock;

public class InvalidBlockAddException extends IllegalAccessException {
    private static final long serialVersionUID = 2064347592399788357L;
    
    public InvalidBlockAddException(ClassicBlock b) {
        super("The block " + b.name + " uses an ID that is already in use!");
    }
    
    public InvalidBlockAddException(ClassicBlock b, String error) {
        super("The block " + b.name + " has caused an error: \"" + error + "\"");
    }

}

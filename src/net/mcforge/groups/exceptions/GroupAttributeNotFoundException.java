package net.mcforge.groups.exceptions;

import java.io.FileNotFoundException;

public class GroupAttributeNotFoundException extends FileNotFoundException {
    public GroupAttributeNotFoundException(String string) {
        super(string);
    }

    private static final long serialVersionUID = -2113767921020055137L;

}

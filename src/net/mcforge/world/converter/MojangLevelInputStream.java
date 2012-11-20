/*******************************************************************************
* Copyright (c) 2012 MCForge.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the GNU Public License v3.0
* which accompanies this distribution, and is available at
* http://www.gnu.org/licenses/gpl.html
******************************************************************************/
package net.mcforge.world.converter;

import net.mcforge.world.converter.MojangLevel;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * Provides for a way of getting a serialized Minecraft(.dat) level without needing the MinecraftServer jar dependency.
 * @author Gamemakergm
 */
public class MojangLevelInputStream extends ObjectInputStream {
    public MojangLevelInputStream(InputStream in) throws IOException {
        super(in);
    }
    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass desc = super.readClassDescriptor();
        if (desc.getName().equals("com.mojang.minecraft.level.Level")) {
            return ObjectStreamClass.lookup(MojangLevel.class);
        }
        return desc;
    }
}

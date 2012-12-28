/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.API;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
* Having a class that implements ManualLoad will tell the PluginHandler
* not to load that object (Weather it be a {@link Plugin} or a {@link Command}) at startup.
* The plugin/command will have to be loaded manually by another plugin/command.
*/
public @interface ManualLoad {
}


package net.mcforge.API;

/**
 * Having a class that implements ManualLoad will tell the PluginHandler
 * not to load that object (Weather it be a {@link Plugin} or a {@link Command}) at startup.
 * The plugin/command will have to be loaded manually by another plugin/command.
 */
public interface ManualLoad { }

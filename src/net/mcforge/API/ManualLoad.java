package net.mcforge.API;

/**
 * Having a class that implements ManualLoad will tell the PluginHandler
 * not to load that object (Weather it be a {@link Plugin} or a {@link Command}) at startup.
 * The plugin/command will have to be loaded manually by another plugin/command.
 */
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ManualLoad {
}

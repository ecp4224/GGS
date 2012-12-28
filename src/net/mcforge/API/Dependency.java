package net.mcforge.API;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Add this annotation above your plugin/command class to give the </br>
 * PluginHandler a dependency list when this plugin/command is loaded.
 */
public @interface Dependency {
    String[] plugins();
}

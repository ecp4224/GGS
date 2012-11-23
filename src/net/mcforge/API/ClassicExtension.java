package net.mcforge.API;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClassicExtension {
    /**
     * The name of the extension
     * @return The name
     */
    String extName();
    /**
     * The version of the extension
     * @return The version, default is 1
     */
    int version() default 1;
}


package org.nebularis.defproxy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides annotations for passing additional string arguments to a
 * {@link org.nebularis.defproxy.annotations.ProxyDelegated} method
 * in order to modify the parameter array during dynamic dispatch.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProxyArguments {

    /**
     * Indicates the end of the parameter list to which additional values will be added.
     */
    public enum Direction {
        Prefix(),
        Suffix();
    }

    /**
     * An array of string arguments to add to the passed parameter list.
     * @return
     */
    String[] value() default {};

    /**
     * The direction in which to fix additional arguments to the existing parameter list.
     * @return
     */
    Direction direction() default Direction.Prefix;
}

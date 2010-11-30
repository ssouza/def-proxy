package org.nebularis.defproxy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TBC: support dependency injection in interface specifications.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProxyInjected {

    /**
     * The key to use when looking up a resource.
     * @return
     */
    String resourceKey();

}

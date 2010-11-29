package org.nebularis.defproxy.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides a means for {@link org.nebularis.defproxy.support.ProxyDelegated}
 * decorated methods to return a different type than that of the mapped method
 * to which they delegate at runtime.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ProxyTypeConverter {
    /**
     * The provider class.
     * @return
     */
    Class<? extends TypeConverter> provider();
}

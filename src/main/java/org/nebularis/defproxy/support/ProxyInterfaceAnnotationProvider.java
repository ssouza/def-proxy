package org.nebularis.defproxy.support;

import java.lang.annotation.*;

/**
 * Provides a means for annotating an annotation
 * to specify that it is a proxy interface provider.
 * In this case, you must provide a delegate accessor
 * that returns a {@link Class}.
 *
 * Example:
 * <pre>
 *      &#064;ProxyInterfaceAnnotationProvider
 *      public @interface MyProxyInterfaceMarker {
 *
 *          Class&lt;?&gt; delegate();
 *
 *          String additionalData default "";
 *      }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ProxyInterfaceAnnotationProvider {}

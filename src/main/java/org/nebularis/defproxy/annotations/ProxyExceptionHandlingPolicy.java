package org.nebularis.defproxy.annotations;

import org.nebularis.defproxy.configuration.ExceptionHandlingPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates an explicit exception handling policy is being set,
 * for the {@link org.nebularis.defproxy.introspection.MethodInvoker}
 * to use at runtime.
 *
 * In order to make this work, the supplied policy type must define
 * a public no-arg constructor.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ProxyExceptionHandlingPolicy {

    /**
     * Pass all unhandled exceptions on to an instance of the stated policy.
     * @return
     */
    Class<? extends ExceptionHandlingPolicy> explicitPolicy();

}

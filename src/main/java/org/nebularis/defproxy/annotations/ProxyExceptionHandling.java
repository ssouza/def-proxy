package org.nebularis.defproxy.annotations;

import org.nebularis.defproxy.configuration.ExceptionHandlingPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates an exception handling policy is set, wrapping
 * all raised errors in an exception of the defined type.
 *
 * In order to make this work, the exception type you specify
 * in {@link ProxyExceptionHandling#wrapWith()} must define
 * a constructor that accepts a single {@link Throwable}
 * subclass instance.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ProxyExceptionHandling {

    /**
     * Wrap all exceptions in an instance of the specified type.
     * @return
     */
    Class<? extends Throwable> wrapWith();

}

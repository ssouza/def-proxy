package org.nebularis.defproxy.configuration;

import org.apache.commons.beanutils.ConstructorUtils;

/**
 * Provides an interface that {@link org.nebularis.defproxy.introspection.MethodInvoker}s
 * use at runtime to handle boundary conditions. 
 */
public abstract class ExceptionHandlingPolicy {

    /**
     * Provides a standard exception handling policy that wraps and re-throws
     * any thrown exception in one of the supplied type.
     * @param wrapperClass
     * @return
     */
    public static ExceptionHandlingPolicy wrapExceptions(final Class<? extends Throwable> wrapperClass) {
        return new ExceptionHandlingPolicy() {
            @Override
            public Object handleException(final Throwable ex) throws Throwable {
                throw ((Throwable) ConstructorUtils.invokeConstructor(wrapperClass, ex));
            }
        };
    }

    /**
     * The heart of the policy: chooses to ignore the supplied exception,
     * re-throw it, or wrap it in another exception and and throw that instead.
     * @param ex
     * @return
     * @throws Throwable
     */
    public abstract Object handleException(final Throwable ex) throws Throwable;

}

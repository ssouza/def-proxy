package org.nebularis.defproxy.support;

import java.lang.reflect.Method;

/**
 * The external behavioural contract for custom method invocations.
 */
public interface MethodInvoker {
    /**
     * 
     * @param delegate
     * @param objects
     * @return
     */
    Object handleInvocation(Object delegate, Object[] objects);
}

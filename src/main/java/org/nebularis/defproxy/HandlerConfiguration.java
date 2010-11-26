package org.nebularis.defproxy;

import org.nebularis.defproxy.support.MethodInvoker;
import org.nebularis.defproxy.support.MethodSignature;

import java.lang.reflect.Method;

/**
 * 
 */
public class HandlerConfiguration {

    // associated an invoker with a method signature
    void registerMethodInvoker(final MethodInvoker mi, final MethodSignature sig) {

    }

    public MethodInvoker getMethodInvoker(final Method method) {
        return null;
    }

}

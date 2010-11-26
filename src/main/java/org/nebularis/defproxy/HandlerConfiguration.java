package org.nebularis.defproxy;

import org.nebularis.defproxy.support.MethodInvoker;
import org.nebularis.defproxy.support.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class HandlerConfiguration {

    private final Map<MethodSignature, MethodInvoker> cache =
            new HashMap<MethodSignature, MethodInvoker>();

    /**
     * Registered a {@link org.nebularis.defproxy.DefaultMethodInvoker} to handle
     * calls to <code>method</code>.
     * @param method the method to register handling for
     */
    void registerMethodInvoker(final Method method) {
        final MethodSignature sig = MethodSignature.fromMethod(method);
        registerMethodInvoker(new DefaultMethodInvoker(sig), sig);
    }

    /**
     * Registered a {@link org.nebularis.defproxy.support.MethodInvoker} to handle
     * calls to <code>method</code>.
     * @param mi the {@link org.nebularis.defproxy.support.MethodInvoker} to register
     * @param method the method to register handling for
     */
    void registerMethodInvoker(final MethodInvoker mi, final Method method) {
        registerMethodInvoker(mi, MethodSignature.fromMethod(method));
    }

    /**
     * Registered a {@link org.nebularis.defproxy.support.MethodInvoker} to handle
     * calls to <code>sig</code>. 
     * @param mi the {@link org.nebularis.defproxy.support.MethodInvoker} to register
     * @param sig the {@link org.nebularis.defproxy.support.MethodSignature} to map it to.
     */
    void registerMethodInvoker(final MethodInvoker mi, final MethodSignature sig) {
        cache.put(sig, mi);
    }

    public MethodInvoker getMethodInvoker(final Method method) throws MethodInvocationNotSupportedException {
        final MethodSignature sig = MethodSignature.fromMethod(method);
        final MethodInvoker invoker = cache.get(sig);
        if (invoker == null) {
            throw new MethodInvocationNotSupportedException(method);
        }
        return invoker;
    }

}

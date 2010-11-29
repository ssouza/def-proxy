package org.nebularis.defproxy.introspection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Simple representation of a "dispatch-able" call site.
 */
public class CallSite {

    private final Object callee;
    private final Method target;
    private final Object[] arguments;

    public CallSite(final Method target, final Object callee, final Object[] arguments) {
        this.target = target;
        this.callee = callee;
        this.arguments = arguments;
    }

    public Object dispatch() throws InvocationTargetException, IllegalAccessException {
        return target.invoke(callee, arguments);
    }
}

package org.nebularis.defproxy;

import java.lang.reflect.Method;

public class MethodInvocationNotSupportedException extends Exception {

    private final Method method;

    public MethodInvocationNotSupportedException(final Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }
}

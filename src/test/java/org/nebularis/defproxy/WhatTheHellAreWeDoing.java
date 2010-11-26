package org.nebularis.defproxy;

import org.junit.Test;
import org.nebularis.defproxy.stubs.MyDelegate;
import org.nebularis.defproxy.stubs.MyProxyInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class WhatTheHellAreWeDoing {

    @Test
    public void canGetName() {
        MyDelegate del = new MyDelegate("Foo");
        assertThat(del.getName(), is(equalTo("Foo")));
    }

    @Test
    public void byDefaultMethodsAreResolvedBasedOnCompleteSignature() {
        final MyDelegate del = new MyDelegate("Foo");
        final MyProxyInterface ifc = wrap(del, MyProxyInterface.class);
        assertThat(ifc.getName(), is(equalTo("Foo")));
    }

    @Test()
    public void nonMatchingMethodSignaturesThrow() {
        final MyDelegate del = new MyDelegate("Foo");
        final MyProxyInterface ifc = wrap(del, MyProxyInterface.class);
    }

    private <T> T wrap(final Object delegateObject, final Class<T> clazz) {
        InvocationHandler handler =
                new InvocationHandler() {
                    private final Object delegate = delegateObject;
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        return ((MyDelegate) delegate).getName();
                    }
                };
        final Class proxyClass = Proxy.getProxyClass(clazz.getClassLoader(), new Class[]{clazz});
        final Object obj = createProxyInstance(handler, proxyClass);
        final T casted = (T) obj;
        return casted;
    }

    static Object createProxyInstance(final InvocationHandler handler, final Class proxyClass) {
        try {
            return proxyClass.getConstructor(new Class[]{InvocationHandler.class}).
                    newInstance(new Object[]{handler});
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}

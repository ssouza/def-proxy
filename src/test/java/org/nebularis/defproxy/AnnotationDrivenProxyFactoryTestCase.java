/*
 * def-proxy
 *
 * Copyright (c) 2010-2011
 * Tim Watson (watson.timothy@gmail.com), Charles Care (c.p.care@gmail.com).
 * All Rights Reserved.
 *
 * This file is provided to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain
 * a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.nebularis.defproxy;

import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nebularis.defproxy.configuration.ProxyConfigurationBuilder;
import org.nebularis.defproxy.introspection.MethodSignature;
import org.nebularis.defproxy.stubs.MyDelegate;
import org.nebularis.defproxy.stubs.MyProxyInterface;
import org.nebularis.defproxy.stubs.SimpleDelegate;
import org.nebularis.defproxy.stubs.SimpleInterface;
import org.nebularis.defproxy.test.AbstractJMockTestSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(JMock.class)
public class AnnotationDrivenProxyFactoryTestCase extends AbstractJMockTestSupport {

    @Test
    public void defaultMappingAreCreatedForAllNonAnnotatedMethods() {
        final ProxyConfigurationBuilder builder = mock(ProxyConfigurationBuilder.class);
        one(builder).delegateMethod(new MethodSignature(void.class, "method1"));

        confirmExpectations();

        final ProxyFactory factory = new AnnotationDrivenProxyFactory();
        factory.createProxy(new SimpleDelegate(), SimpleInterface.class, builder);
    }

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
                        // handlerConfiguration.getMethodInvoker(method).handleInvocation(delegate, objects);
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

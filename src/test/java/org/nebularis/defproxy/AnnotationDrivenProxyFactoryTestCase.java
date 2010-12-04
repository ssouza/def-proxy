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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.nebularis.defproxy.annotations.Insertion.Prefix;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.HashMap;

import junit.framework.Assert;

import org.apache.commons.lang.NotImplementedException;
import org.jmock.integration.junit4.JMock;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nebularis.defproxy.annotations.*;
import org.nebularis.defproxy.configuration.ProxyConfigurationBuilder;
import org.nebularis.defproxy.introspection.MethodSignature;
import org.nebularis.defproxy.introspection.TypeConverter;
import org.nebularis.defproxy.introspection.TypeConverterFactory;
import org.nebularis.defproxy.stubs.*;
import org.nebularis.defproxy.test.AbstractJMockTestSupport;

@Ignore
@RunWith(JMock.class)
public class AnnotationDrivenProxyFactoryTestCase extends AbstractJMockTestSupport {

    // null values as input should fail
    @Test(expected = IllegalArgumentException.class)
    public void passingNullValuesToDelegateShouldFail() {
        AnnotationDrivenProxyFactory factory = new AnnotationDrivenProxyFactory();
        factory.createProxy(null, Baz.class);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void passingNullValuesToProxyInterfaceShouldFail() {
        AnnotationDrivenProxyFactory factory = new AnnotationDrivenProxyFactory();
        factory.createProxy(new Object(), null);
        Assert.fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void passingNullValuesToProxyConfigurationBuilderShouldFail() {
        AnnotationDrivenProxyFactory factory = new AnnotationDrivenProxyFactory();
        factory.createProxy(new String(), Baz.class, null);
        Assert.fail();
    }

    // mapping proxy interface to a delegate that isn't registered in the ProxyInterface annotation should fail
    @Test(expected = InvalidProxyConfigurationException.class)
    public void passingNonAnnotatedInterface() {
        AnnotationDrivenProxyFactory factory = new AnnotationDrivenProxyFactory();
        factory.createProxy(new Object(), Baz.class);
        Assert.fail();
    }

    @Test(expected = InvalidProxyConfigurationException.class)
    public void passingUnregisteredDelegateShouldFail() {
        final ProxyFactory factory = new AnnotationDrivenProxyFactory();
        factory.createProxy(new Object(), SimpleInterface.class);
        Assert.fail();
    }

    @Test
    public void passingRegisteredDelegateShouldPass() {
        AnnotationDrivenProxyFactory factory = new AnnotationDrivenProxyFactory();
        factory.createProxy(new StoredItem(), Item.class);
    }

    @ProxyInterface(delegate = StoredItem.class)
    public interface TestMethods {
        @ProxyMethod(methodName = "getProductId")
        int test();
    }

    // mapping methods with methodName property set, should call builder (see builder test case)
    @Test
    public void testAllMethodsAreDelegatedOnProxy() {
        AnnotationDrivenProxyFactory factory = new AnnotationDrivenProxyFactory();
        final ProxyConfigurationBuilder builder = mock(ProxyConfigurationBuilder.class);
        one(builder).delegateMethod(new MethodSignature(Integer.TYPE, "test"), new MethodSignature(Integer.TYPE, "getProductId"));
        confirmExpectations();
        factory.createProxy(new StoredItem(), TestMethods.class, builder);
    }

    @ProxyInterface(delegate = StoredItem.class)
    public interface TestMethodsWithoutAnnotation {
        int getProductId();
    }

    // mapping methods not using ProxyDelegated should call builder.delegateMethod(interfaceMethod);
    @Test
    public void testAllMethodsAreDelegatedOnProxyEvenWithoutAnnotation() {
        AnnotationDrivenProxyFactory factory = new AnnotationDrivenProxyFactory();
        final ProxyConfigurationBuilder builder = mock(ProxyConfigurationBuilder.class);
        one(builder).delegateMethod(new MethodSignature(Integer.TYPE, "getProductId"));
        confirmExpectations();
        factory.createProxy(new StoredItem(), TestMethodsWithoutAnnotation.class, builder);
    }

    // what about overloads?
    // what about multiple return types?

    @ProxyInterface(delegate = HashMap.class)
    @ProxyTypeConverterFactory(provider = IntOfStringConverter.class)
    public interface TestClassWithTypeConverter {
        @ProxyMethod(methodName = "get")
        @ProxyArguments(value = {"bar-code"}, direction = Prefix)
        int barcode();

        @ProxyMethod(methodName = "get")
        @ProxyArguments(value = {"product-id"}, direction = Prefix)
        int productId();

        @ProxyMethod(methodName = "get")
        @ProxyArguments(value = {"end-date"}, direction = Prefix)
        @ProxyTypeConverter(provider = DateParserTypeConverter.class)
        Date endDate();
    }

    @Ignore
    @Test
    public void testClassLevelTypeConverterWithMethodLevelOverrideRegistration() {
        final AnnotationDrivenProxyFactory factory = new AnnotationDrivenProxyFactory();
        final ProxyConfigurationBuilder builder = mock(ProxyConfigurationBuilder.class);

        one(builder).setTypeConverterFactory((TypeConverterFactory) with(instanceOf(IntOfStringConverter.class)));

        final MethodSignature barCodeMethod = new MethodSignature(int.class, "barcode");
        one(builder).wrapDelegate(Insertion.Prefix, barCodeMethod, "bar-code");
        one(builder).delegateMethod(barCodeMethod, new MethodSignature(Object.class, "get"));
        one(builder).setTypeConverter(with(barCodeMethod), (TypeConverter) with(instanceOf(IntOfStringConverter.class)));

        final MethodSignature productIdMethod = new MethodSignature(int.class, "productId");
        one(builder).wrapDelegate(Insertion.Prefix, productIdMethod, "product-id");
        one(builder).delegateMethod(productIdMethod, new MethodSignature(Object.class, "get"));
        one(builder).setTypeConverter(with(productIdMethod), (TypeConverter) with(instanceOf(IntOfStringConverter.class)));
        
        final MethodSignature endDateMethod = new MethodSignature(Date.class, "endDate");
        one(builder).wrapDelegate(Insertion.Prefix, endDateMethod, "end-date");
        one(builder).delegateMethod(productIdMethod, new MethodSignature(Object.class, "get"));
        one(builder).setTypeConverter(with(productIdMethod), (TypeConverter) with(instanceOf(DateParserTypeConverter.class)));

        confirmExpectations();

        factory.createProxy(new HashMap(), TestClassWithTypeConverter.class, builder);
    }

    @ProxyInterface(delegate = StoredItem.class)
    public interface TestMethodsWithProxyArgumentAnnotation {
        @ProxyMethod(methodName = "get")
        @ProxyArguments(value = {"bar-code"}, direction = Prefix)
        String barcode();
    }

    // mapping methods with ProxyArguments set, should call builder.wrapDelegate (see builder test case)
    @Test
    public void testAllMethodWithProxyArgumentsSetShouldWrapTheResult() {
        AnnotationDrivenProxyFactory factory = new AnnotationDrivenProxyFactory();
        final ProxyConfigurationBuilder builder = mock(ProxyConfigurationBuilder.class);
        one(builder).wrapDelegate(Insertion.Prefix, new MethodSignature(String.class, "barcode"), "bar-code");
        one(builder).delegateMethod(new MethodSignature(String.class, "barcode"), new MethodSignature(String.class, "get"));
        confirmExpectations();
        factory.createProxy(new StoredItem(), TestMethodsWithProxyArgumentAnnotation.class, builder);
    }


    // mapping methods with methodInvocationHandler set, should call builder (see builder test case)
    @ProxyInterface(delegate = StoredItem.class)
    public interface TestMethodsWithProxyTypeConverterSet {
        @ProxyMethod(methodName = "get")
        @ProxyArguments(value = {"bar-code"}, direction = Prefix)
        @ProxyTypeConverter(provider = IntOfStringConverter.class)
        String barcode();
    }

    public class SuperStoredItem extends StoredItem {
    }

    // mapping methods with ProxyArguments set, should call builder.wrapDelegate (see builder test case)
    @Test
    public void testAllMethodWithProxyTypeConverterSet() {
        if (true) throw new NotImplementedException();
        AnnotationDrivenProxyFactory factory = new AnnotationDrivenProxyFactory();
        final ProxyConfigurationBuilder builder = mock(ProxyConfigurationBuilder.class);
        one(builder).wrapDelegate(Insertion.Prefix, new MethodSignature(String.class, "barcode"), "bar-code");
        one(builder).delegateMethod(new MethodSignature(String.class, "barcode"), new MethodSignature(String.class, "get"));

        confirmExpectations();
        factory.createProxy(new SuperStoredItem(), TestMethodsWithProxyTypeConverterSet.class, builder);
    }


    // getting annotations from methods should be done in a safe/defensive manner

    // getting annotations from methods should work across inheritance hierarchies.

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

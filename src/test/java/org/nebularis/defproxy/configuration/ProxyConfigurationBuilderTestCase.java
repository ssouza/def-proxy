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
package org.nebularis.defproxy.configuration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.jmock.integration.junit4.JMock;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nebularis.defproxy.annotations.Insertion;
import org.nebularis.defproxy.introspection.*;
import org.nebularis.defproxy.stubs.Baz;
import org.nebularis.defproxy.stubs.FooBar;
import org.nebularis.defproxy.stubs.Item;
import org.nebularis.defproxy.stubs.MyDelegate;
import org.nebularis.defproxy.stubs.MyProxyInterface;
import org.nebularis.defproxy.stubs.SimpleDelegate;
import org.nebularis.defproxy.stubs.SimpleInterface;
import org.nebularis.defproxy.stubs.SomeProxyInterface;
import org.nebularis.defproxy.stubs.StoredItem;
import org.nebularis.defproxy.test.AbstractJMockTestSupport;
import org.nebularis.defproxy.validation.MethodSignatureValidator;

@RunWith(JMock.class)
public class ProxyConfigurationBuilderTestCase extends AbstractJMockTestSupport {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBeAbleToInstantiateWithNullProxyClass() {
        new ProxyConfigurationBuilder(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBeAbleToInstantiateWithNullDelegateClass() {
        new ProxyConfigurationBuilder(MyProxyInterface.class, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void addingInterfaceMethodMappingShouldThrowForNullValues() throws InvalidMethodMappingException {
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getItemName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, Baz.class);
        builder.delegateMethod(null, delegateMethod);
    }

    @Test(expected=IllegalArgumentException.class)
    public void addingDelegateMethodMappingShouldThrowForNullValues() throws InvalidMethodMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = null;
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, Baz.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
    }

    @Ignore("Migrating to MethodSignature")
    @Test
    public void invalidInterfaceMethodNamesWillThrow() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getNaame");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateProxyConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(interfaceMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyProxyInterface.class)));
        }
    }

    @Ignore("Migrating to MethodSignature")
    @Test
    public void invalidInterfaceReturnTypesWillThrow() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(void.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateProxyConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(interfaceMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyProxyInterface.class)));
        }
    }

    @Ignore("Migrating to MethodSignature")
    @Test
    public void invalidInterfaceParameterTypesWillThrow() throws MappingException {
        final MethodSignature interfaceMethod =
                new MethodSignature(void.class, "checkIdentity", String.class, String.class);
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateProxyConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(interfaceMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyProxyInterface.class)));
        }
    }

    @Ignore("Migrating to MethodSignatureTranslator")
    @Test
    public void invalidDelegateMethodNamesWillThrow() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getFlobby");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateProxyConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(delegateMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyDelegate.class)));
        }
    }

    @Ignore("Migrating to MethodSignatureTranslator - THIS TEST IS NO LONGER VALID")
    @Test
    public void invalidDelegateReturnTypesWillThrow() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(void.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateProxyConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(delegateMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyDelegate.class)));
        }
    }

    @Test
    public void invalidDelegateParameterTypesWillThrow() throws MappingException {
        final MethodSignature interfaceMethod =
                new MethodSignature(void.class, "checkIdentity", String.class, int.class);
        final MethodSignature delegateMethod =
                new MethodSignature(void.class, "checkIdentity", String.class, String.class);
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateProxyConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(delegateMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyDelegate.class)));
        }
    }

    @Test
    public void correctMappingsDoNotFail() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.generateProxyConfiguration();
    }

    @Ignore("Subsumed by MethodSignatureTranslator")
    @Test(expected = IncompatibleMethodMappingException.class)
    public void mappingToMethodWithIncompatibleReturnTypeWillThrow() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(Integer.class, "getInteger");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.generateProxyConfiguration();
    }

    @Test(expected = IncompatibleMethodMappingException.class)
    public void mappingToMethodWithIncompatibleParametersWillThrow() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getEmptyString", Boolean.class);
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.generateProxyConfiguration();
    }

    @Test(expected = IncompatibleMethodMappingException.class)
    public void incompatibleMethodSignaturesWillThrow() throws IncompatibleMethodMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName", Integer.class);
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName", String.class);
        verifyCompatibility(interfaceMethod, delegateMethod);
    }

    @Test(expected = IncompatibleMethodMappingException.class)
    public void incompatibleWrongNumberOfParamsInMethodSignatureWillThow() throws IncompatibleMethodMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName", String.class, Integer.class);
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName", String.class);
        verifyCompatibility(interfaceMethod, delegateMethod);
    }

    @Test(expected = IncompatibleMethodMappingException.class)
    public void incompatibleReturnTypesWillThrow() throws IncompatibleMethodMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(Integer.class, "getName");
        verifyCompatibility(interfaceMethod, delegateMethod);
    }

    @Test
    public void compatibleMethodSignaturesWillNotThrow() throws IncompatibleMethodMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName", Integer.class);
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName", Integer.class);
        verifyCompatibility(interfaceMethod, delegateMethod);
    }

    @Test(expected = IncompatibleMethodMappingException.class)
    public void compatibleContraVariantReturnTypesWillThrow() throws IncompatibleMethodMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName", Integer.class);
        final MethodSignature delegateMethod = new MethodSignature(Object.class, "getName", Integer.class);
        verifyCompatibility(interfaceMethod, delegateMethod);
    }

    @Test
    public void compatibleCovariantReturnTypesWillNotThrow() throws IncompatibleMethodMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(Object.class, "getName", Integer.class);
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName", Integer.class);
        verifyCompatibility(interfaceMethod, delegateMethod);
    }

    @Test
    public void compatibleCovariantParametersWillNotThrow() throws IncompatibleMethodMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName", Number.class);
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName", Integer.class);
        verifyCompatibility(interfaceMethod, delegateMethod);
    }

    @Test(expected = IncompatibleMethodMappingException.class)
    public void compatibleContraVariantParametersWillThrow() throws IncompatibleMethodMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName", Integer.class);
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName", Number.class);
        verifyCompatibility(interfaceMethod, delegateMethod);
    }

    @Test
    public void directMappingProducesEqualMethodSignatures() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod);
        assertThat(builder.getDelegatedMethod(interfaceMethod), is(equalTo(delegateMethod)));
    }

    @Test(expected = InvalidMethodMappingException.class)
    public void directMappingFailsForUnmatchedDelegateMethods() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getSpecialName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod);
        assertThat(builder.getDelegatedMethod(interfaceMethod), is(equalTo(interfaceMethod)));
        builder.generateProxyConfiguration();
    }

    @Test
    public void viaMappingWorksForMatchedDelegateMethods() throws MappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getSpecialName");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateViaMethod(interfaceMethod, "getName");
        assertThat(builder.getDelegatedMethod(interfaceMethod), is(equalTo(delegateMethod)));
        builder.generateProxyConfiguration();
    }

    @Test
    public void typeConversionOverridesTargetMethodReturnType() throws Throwable {
        final TypeConverter converter = mock(TypeConverter.class);
        one(converter).getInputType();
        will(returnValue(String.class));
        one(converter).getOutputType();
        will(returnValue(Integer.class));
        one(converter).convert(with("59"));
        will(returnValue(59));
        confirmExpectations();

        final StoredItem item = new StoredItem();
        item.set("productId", "59");

        final MethodSignature interfaceMethod = new MethodSignature(int.class, "productId");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getProductId");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(Item.class, StoredItem.class);

        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.setTypeConverter(interfaceMethod, converter);
        final ProxyConfiguration configuration = builder.generateProxyConfiguration();
        final MethodInvoker invoker = configuration.getMethodInvoker(Item.class.getMethod("productId"));
        invoker.setTypeConverter(converter);

        invoker.handleInvocation(item);
    }

    @Test
    public void factoryTypeConversionOverridesTargetMethodReturnType() throws Throwable {
        final TypeConverter converter = mock(TypeConverter.class);
        one(converter).getInputType();
        will(returnValue(String.class));
        one(converter).getOutputType();
        will(returnValue(Integer.class));
        
        final TypeConverterFactory typeConverterFactory = mock(TypeConverterFactory.class);
        allowing(typeConverterFactory).createTypeConverter(with(String.class), with(int.class));
        will(returnValue(converter));

        confirmExpectations();

        final StoredItem item = new StoredItem();
        item.set("productId", "59");

        final MethodSignature interfaceMethod = new MethodSignature(int.class, "productId");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getProductId");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(Item.class, StoredItem.class);
        builder.setTypeConverterFactory(typeConverterFactory);

        builder.delegateMethod(interfaceMethod, delegateMethod);
        final ProxyConfiguration configuration = builder.generateProxyConfiguration();

        final MethodInvoker invoker = configuration.getMethodInvoker(Item.class.getMethod("productId"));
        assertThat(invoker.getTypeConverter(), is(sameInstance(converter)));
    }

    @Test
    public void methodLevelTypeConverterOverridesClassLevel() throws Throwable {
        final TypeConverter converter = mock(TypeConverter.class);
        one(converter).getInputType();
        will(returnValue(String.class));
        one(converter).getOutputType();
        will(returnValue(Integer.class));

        final TypeConverterFactory typeConverterFactory = mock(TypeConverterFactory.class);
        allowing(typeConverterFactory).createTypeConverter(with(String.class), with(int.class));
        will(throwException(new RuntimeException()));

        confirmExpectations();

        final StoredItem item = new StoredItem();
        item.set("productId", "59");

        final MethodSignature interfaceMethod = new MethodSignature(int.class, "productId");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getProductId");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(Item.class, StoredItem.class);
        builder.setTypeConverterFactory(typeConverterFactory);

        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.setTypeConverter(interfaceMethod, converter);
        final ProxyConfiguration configuration = builder.generateProxyConfiguration();

        final MethodInvoker invoker = configuration.getMethodInvoker(Item.class.getMethod("productId"));
        assertThat(invoker.getTypeConverter(), is(sameInstance(converter)));
    }

    @Test
    public void argumentOverridesSplatTargetMethodArity() throws Throwable {
        final TypeConverter converter = mock(TypeConverter.class);
        one(converter).getInputType();
        will(returnValue(Object.class));
        one(converter).getOutputType();
        will(returnValue(Integer.class));
        one(converter).convert(with("59"));
        will(returnValue(59));
        confirmExpectations();

        final Map<String, String> hash = new HashMap<String, String>();
        hash.put("product-id", "59");

        final MethodSignature interfaceMethod = new MethodSignature(int.class, "productId");
        final MethodSignature delegateMethod = new MethodSignature(Object.class, "get", Object.class);
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(Item.class, HashMap.class);

        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.wrapDelegate(Insertion.Prefix, delegateMethod, "product-id");
        builder.setTypeConverter(interfaceMethod, converter);

        final ProxyConfiguration configuration = builder.generateProxyConfiguration();
        final MethodInvoker invoker = configuration.getMethodInvoker(Item.class.getMethod("productId"));
        invoker.setTypeConverter(converter);

        assertThat((Integer) invoker.handleInvocation(hash), is(equalTo(59)));
    }

    @Test(expected = IncompatibleMethodMappingException.class)
    public void argumentOverridesFailConstructionWhenWrapperArityIsTooHigh() throws Throwable {
        final TypeConverter converter = new TypeConverter() {
            @Override
            public Class getInputType() { return Object.class; }

            @Override
            public Class getOutputType() { return Integer.class; }

            @Override
            public Object convert(final Object o) { throw new UnsupportedOperationException(); }
        };

        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(Item.class, HashMap.class);

        final MethodSignature interfaceMethod = new MethodSignature(int.class, "productId");
        final MethodSignature delegateMethod = new MethodSignature(Object.class, "get", Object.class);

        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.wrapDelegate(Insertion.Prefix, delegateMethod, "product-id", "this-is-one-arg-too-many!");
        builder.setTypeConverter(interfaceMethod, converter);

        builder.generateProxyConfiguration();
    }

    @Test
    public void argumentOverridesPanOutWhenWrapperArityIsLowerThanExpectation() throws Throwable {
        final TypeConverter converter = new TypeConverter() {
            @Override
            public Class getInputType() { return Object.class; }

            @Override
            public Class getOutputType() { return boolean.class; }

            @Override
            public Object convert(final Object o) { throw new UnsupportedOperationException(); }
        };

        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(SomeProxyInterface.class, FooBar.class);

        final MethodSignature interfaceMethod = new MethodSignature(boolean.class, "strangeMethod");
        final MethodSignature delegateMethod =
                new MethodSignature(boolean.class, "checkCompatibility", FooBar.class, String.class);

        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.wrapDelegate(Insertion.Prefix, delegateMethod, new FooBar() /* we let string arrive at runtime! */);
        builder.setTypeConverter(interfaceMethod, converter);

        builder.generateProxyConfiguration();
    }

    @Test
    public void partialArgumentOverridesResultInLooseTypeCheckingOfRemainingParameters() throws Throwable {
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(SomeProxyInterface.class, FooBar.class);

        final MethodSignature interfaceMethod = new MethodSignature(boolean.class, "strangeMethod", String.class);
        final MethodSignature delegateMethod =
                new MethodSignature(boolean.class, "checkCompatibility", FooBar.class, String.class);

        final FooBar presetFoobar = new FooBar();
        final FooBar subject = mock(FooBar.class);
        one(subject).checkCompatibility(with(same(presetFoobar)), with("string123"));
        will(returnValue(true));
        confirmExpectations();

        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.wrapDelegate(Insertion.Prefix, delegateMethod, presetFoobar /* we let string arrive at runtime! */);

        final ProxyConfiguration configuration = builder.generateProxyConfiguration();
        final MethodInvoker invoker = configuration.getMethodInvoker(
                SomeProxyInterface.class.getMethod("strangeMethod", String.class));

        assertThat((Boolean) invoker.handleInvocation(subject, "string123"), is(equalTo(true)));
    }

    @Test(expected = IncompatibleMethodMappingException.class)
    public void argumentOverridesFailConstructionWhenTypesDoNotMatch() throws Throwable {
        final TypeConverter converter = new TypeConverter() {
            @Override
            public Class getInputType() { return Object.class; }

            @Override
            public Class getOutputType() { return boolean.class; }

            @Override
            public Object convert(final Object o) { throw new UnsupportedOperationException(); }
        };

        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(SomeProxyInterface.class, FooBar.class);

        final MethodSignature interfaceMethod = new MethodSignature(boolean.class, "strangeMethod");
        final MethodSignature delegateMethod =
                new MethodSignature(boolean.class, "checkCompatibility", FooBar.class, String.class);

        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.wrapDelegate(Insertion.Suffix, delegateMethod, "this-arg-is-in-the-wrong-position!", new FooBar());
        builder.setTypeConverter(interfaceMethod, converter);

        builder.generateProxyConfiguration();
    }

    @Test
    public void methodLevelExceptionHandlingPolicyIsPassedToInvoker() throws Throwable {
        final ExceptionHandlingPolicy policy = stub(ExceptionHandlingPolicy.class);
        confirmExpectations();

        final StoredItem item = new StoredItem();
        item.set("productId", "59");

        final MethodSignature interfaceMethod = new MethodSignature(void.class, "method1");
        final MethodSignature delegateMethod = new MethodSignature(void.class, "method1");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(SimpleInterface.class, SimpleDelegate.class);

        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.setExceptionHandlingPolicy(interfaceMethod, policy);
        final ProxyConfiguration configuration = builder.generateProxyConfiguration();

        final MethodInvoker invoker = configuration.getMethodInvoker(SimpleInterface.class.getMethod("method1"));
        assertThat(invoker.getExceptionHandlingPolicy(), is(sameInstance(policy)));
    }

    @Test
    public void typeLevelExceptionHandlingPolicyIsPassedToInvoker() throws Throwable {
        final ExceptionHandlingPolicy policy = stub(ExceptionHandlingPolicy.class);
        confirmExpectations();

        final StoredItem item = new StoredItem();
        item.set("productId", "59");

        final MethodSignature interfaceMethod = new MethodSignature(void.class, "method1");
        final MethodSignature delegateMethod = new MethodSignature(void.class, "method1");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(SimpleInterface.class, SimpleDelegate.class);
        builder.setGlobalExceptionHandlingPolicy(policy);

        builder.delegateMethod(interfaceMethod, delegateMethod);
        final ProxyConfiguration configuration = builder.generateProxyConfiguration();

        final MethodInvoker invoker = configuration.getMethodInvoker(SimpleInterface.class.getMethod("method1"));
        assertThat(invoker.getExceptionHandlingPolicy(), is(sameInstance(policy)));
    }

    @Test
    public void methodLevelExceptionHandlingPolicyOverridesTypeLevel() throws Throwable {
        final ExceptionHandlingPolicy globalPolicy = mock(ExceptionHandlingPolicy.class, "globalEHP");
        final ExceptionHandlingPolicy localPolicy = mock(ExceptionHandlingPolicy.class, "localEHP");
        confirmExpectations();

        final StoredItem item = new StoredItem();
        item.set("productId", "59");

        final MethodSignature interfaceMethod = new MethodSignature(void.class, "method1");
        final MethodSignature delegateMethod = new MethodSignature(void.class, "method1");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(SimpleInterface.class, SimpleDelegate.class);
        builder.setGlobalExceptionHandlingPolicy(globalPolicy);

        builder.delegateMethod(interfaceMethod, delegateMethod);
        builder.setExceptionHandlingPolicy(interfaceMethod, localPolicy);
        final ProxyConfiguration configuration = builder.generateProxyConfiguration();

        final MethodInvoker invoker = configuration.getMethodInvoker(SimpleInterface.class.getMethod("method1"));
        assertThat(invoker.getExceptionHandlingPolicy(), is(sameInstance(localPolicy)));
    }

    private void verifyCompatibility(MethodSignature interfaceMethod, MethodSignature delegateMethod) throws IncompatibleMethodMappingException {
        final ProxyConfigurationBuilder builder = getProxyConfigurationBuilderWithNoClassLevelChecking();
        builder.checkCompatibility(interfaceMethod, delegateMethod);
    }

    private ProxyConfigurationBuilder getProxyConfigurationBuilderWithNoClassLevelChecking() {
        return configureBuilderWithStubValidators(new ProxyConfigurationBuilder(Object.class, Object.class));
    }

    private ProxyConfigurationBuilder configureBuilderWithStubValidators(final ProxyConfigurationBuilder builder) {
        final MethodSignatureValidator mock1 = mock(MethodSignatureValidator.class, "validator1");
        final MethodSignatureValidator mock2 = mock(MethodSignatureValidator.class, "validator2");
        ignoring(mock1);
        ignoring(mock2);
        confirmExpectations();

        builder.setInterfaceValidator(mock1);
        builder.setDelegateValidator(mock2);
        return builder;
    }
    
}
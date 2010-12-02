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

import java.lang.reflect.Method;

import org.apache.commons.lang.Validate;
import org.nebularis.defproxy.annotations.*;
import org.nebularis.defproxy.configuration.ProxyConfigurationBuilder;
import org.nebularis.defproxy.introspection.MethodSignature;
import org.nebularis.defproxy.introspection.TypeConverter;
import org.nebularis.defproxy.introspection.TypeConverterFactory;

import static org.nebularis.defproxy.introspection.ReflectionUtils.isAssignable;

/**
 * {@link org.nebularis.defproxy.ProxyFactory} implementation that uses
 * class/method level annotations to handle proxy configuration.
 */
public class AnnotationDrivenProxyFactory implements ProxyFactory {

    @Override
    public <T> T createProxy(final Object delegate, final Class<T> proxyInterface) {
        Validate.notNull(delegate, "Please specify the delegate");
        Validate.notNull(proxyInterface, "Please specify the proxy interface");
        Class<? extends Object> delegateClazz = delegate.getClass();
        return createProxy(delegate, proxyInterface, new ProxyConfigurationBuilder(proxyInterface, delegateClazz));
    }

    @Override
    public <T> T createProxy(final Object delegate, final Class<T> proxyInterface, final ProxyConfigurationBuilder builder) throws InvalidProxyConfigurationException {
        Validate.notNull(delegate, "Please specify the delegate");
        Validate.notNull(proxyInterface, "Please specify the proxy interface");
        Validate.notNull(builder, "Please specify the Proxy Configuration builder");

        verifyProxyInterface(delegate, proxyInterface, builder);
        final TypeConverterFactory converterFactory = configureTypeLevelConverter(proxyInterface, builder);

        for (final Method wrapperMethod : proxyInterface.getMethods()) {

            final Class<?> returnType = wrapperMethod.getReturnType();
            final String interfaceMethodName = wrapperMethod.getName();
            MethodSignature interfaceMethod = new MethodSignature(returnType, interfaceMethodName);

            final ProxyArguments proxyArguments = wrapperMethod.getAnnotation(ProxyArguments.class);
            if (proxyArguments != null) {
                builder.wrapDelegate(proxyArguments.direction(), interfaceMethod, proxyArguments.value());
            }

            final TypeConverter methodConverter =
                    configureMethodLevelConverter(builder, wrapperMethod, interfaceMethod, proxyInterface);

            final ProxyMethod methodAnnotation = wrapperMethod.getAnnotation(ProxyMethod.class);
            if (methodAnnotation != null) {
                String delegateMethodName = methodAnnotation.methodName();
                MethodSignature delegateMethod = null;

                final Method[] candidateMethods = delegate.getClass().getMethods();
                for (final Method targetMethod : candidateMethods) {
                    if (!targetMethod.getName().equals(delegateMethodName)) {
                        continue;
                    }
                    // are the method return types compatible?
                    if (!isAssignable(wrapperMethod.getReturnType(), targetMethod.getReturnType())) {
                        // is there a method level type converter definition that supports this mapping?
                        if (!isConvertible(methodConverter,
                                    targetMethod.getReturnType(), wrapperMethod.getReturnType())) {
                            // if the return types are not compatible, is there a type level converter provider?
                            // if so, we will *assume* that the difference in return types is not relevant
                            if (converterFactory == null) {
                                // no local or global type conversion is available, so we can't map to this method!!!
                                continue;
                            }
                        }
                    }
                    if (isAssignable(wrapperMethod.getParameterTypes(), targetMethod.getParameterTypes())) {
                        delegateMethod = MethodSignature.fromMethod(targetMethod);
                        continue;
                    }
                    System.out.println("foo");
                }

                // we haven't found a target method, therefore we're out of here....
                if (delegateMethod == null) {
                    throw new InvalidProxyConfigurationException(interfaceMethod, proxyInterface,
                            "Interface method cannot be mapped to a method on the target (delegate) class");
                }

                builder.delegateMethod(interfaceMethod, delegateMethod);
            } else {
                builder.delegateMethod(interfaceMethod);
            }

        }

        return null;
    }

    private boolean isConvertible(final TypeConverter converter, final Class<?> inputType, final Class<?> outputType) {
        if (converter == null)
            return false;
        return isAssignable(converter.getInputType(), inputType) &&
               isAssignable(converter.getOutputType(), outputType);
    }

    private TypeConverter configureMethodLevelConverter(final ProxyConfigurationBuilder builder, final Method method, final MethodSignature interfaceMethod, final Class<?> proxyInterface) {
        final ProxyTypeConverter proxyTypeConverter = method.getAnnotation(ProxyTypeConverter.class);
        if (proxyTypeConverter != null) {
            try {
                final TypeConverter converter = proxyTypeConverter.provider().newInstance();
                builder.setTypeConverter(interfaceMethod, converter);
                return converter;
            } catch (Exception e) {
                throw new InvalidProxyConfigurationException(proxyTypeConverter, proxyInterface,
                        "Unable to instantiate ProxyTypeConverterFactory class1.", e);
            }
        }
    }

    private TypeConverterFactory configureTypeLevelConverter(final Class<?> proxyInterface, final ProxyConfigurationBuilder builder) {
        final ProxyTypeConverterFactory proxyTypeConverter = proxyInterface.getAnnotation(ProxyTypeConverterFactory.class);
        if (proxyTypeConverter != null) {
            try {
                final TypeConverterFactory converterFactory = proxyTypeConverter.provider().newInstance();
                builder.setTypeConverterFactory(converterFactory);
                return converterFactory;
            } catch (Exception e) {
                throw new InvalidProxyConfigurationException(proxyTypeConverter, proxyInterface,
                        "Unable to instantiate ProxyTypeConverterFactory class1.", e);
            } 
        }
    }

    private <T> void verifyProxyInterface(final Object delegate, final Class<T> proxyInterface, final ProxyConfigurationBuilder builder) {
        // TODO: use ProxyInterfaceAnnotationProvider instead
        ProxyInterface annotation = proxyInterface.getAnnotation(ProxyInterface.class);
        if (annotation == null) {
            throw new InvalidProxyConfigurationException(delegate, proxyInterface);
        }
        if (!isAssignable(annotation.delegate(), delegate.getClass())) {
            throw new InvalidProxyConfigurationException(delegate, proxyInterface);
        }
    }


}

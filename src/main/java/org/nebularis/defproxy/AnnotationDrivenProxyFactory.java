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
import org.nebularis.defproxy.annotations.ProxyArguments;
import org.nebularis.defproxy.annotations.ProxyInterface;
import org.nebularis.defproxy.annotations.ProxyMethod;
import org.nebularis.defproxy.annotations.ProxyTypeConverter;
import org.nebularis.defproxy.configuration.ProxyConfigurationBuilder;
import org.nebularis.defproxy.introspection.MethodSignature;

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
    public <T> T createProxy(final Object delegate, final Class<T> proxyInterface, final ProxyConfigurationBuilder builder) throws UnproxyableDelegateException {
        Validate.notNull(delegate, "Please specify the delegate");
        Validate.notNull(proxyInterface, "Please specify the proxy interface");
        Validate.notNull(builder, "Please specify the Proxy Configuration builder");

        verifyProxyInterface(delegate, proxyInterface);

        for (Method method : proxyInterface.getMethods()) {

            //Get the annotations from the method
            ProxyMethod methodAnnotation = method.getAnnotation(ProxyMethod.class);
            ProxyArguments proxyArguments = method.getAnnotation(ProxyArguments.class);
            ProxyTypeConverter proxyTypeConverter = method.getAnnotation(ProxyTypeConverter.class);

            Class<?> returnType = method.getReturnType();
            String interfaceMethodName = method.getName();
            MethodSignature interfaceMethod = new MethodSignature(returnType, interfaceMethodName);
            //Check if any additional proxy arguments are provided
            if (proxyArguments != null) {
                builder.wrapDelegate(proxyArguments.direction(), interfaceMethod, proxyArguments.value());
            }

            if (methodAnnotation != null) {
                String delegateMethodName = methodAnnotation.methodName();
                MethodSignature delegateMethod = new MethodSignature(returnType, delegateMethodName);
                builder.delegateMethod(interfaceMethod, delegateMethod);
            } else {
                builder.delegateMethod(interfaceMethod);
            }
        }

        return null;
    }

    private <T> void verifyProxyInterface(final Object delegate, final Class<T> proxyInterface) {
        // TODO: use ProxyInterfaceAnnotationProvider instead
        ProxyInterface annotation = proxyInterface.getAnnotation(ProxyInterface.class);
        if (annotation == null) {
            throw new UnproxyableDelegateException(delegate);
        }
        if (!isAssignable(annotation.delegate(), delegate.getClass())) {
            throw new UnproxyableDelegateException(delegate);
        }
    }


}

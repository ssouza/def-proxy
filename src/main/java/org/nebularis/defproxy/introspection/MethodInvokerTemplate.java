/*
 *
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
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.nebularis.defproxy.introspection;

import org.nebularis.defproxy.configuration.ExceptionHandlingPolicy;
import org.nebularis.defproxy.introspection.CallSite;
import org.nebularis.defproxy.introspection.MethodSignature;
import org.nebularis.defproxy.introspection.MethodInvoker;
import org.nebularis.defproxy.utils.TypeConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.apache.commons.beanutils.MethodUtils.getMatchingAccessibleMethod;

/**
 * Default, reflection based {@link org.nebularis.defproxy.introspection.MethodInvoker},
 * providing additional support for template methods. Subclasses can override the
 * details of the target site prior to method invocation (by overriding the
 * {@link org.nebularis.defproxy.introspection.MethodInvokerTemplate#beforeInvocation(Object, java.lang.reflect.Method, Object[])}
 * method), or alter the return value
 * (overriding the {@link org.nebularis.defproxy.introspection.MethodInvokerTemplate#afterInvocation(Object, CallSite)} method).
 */
public class MethodInvokerTemplate implements MethodInvoker {

    private final MethodSignature sig;
    private TypeConverter converter;
    private ExceptionHandlingPolicy policy = new ExceptionHandlingPolicy() {
        @Override
        public Object handleException(final Throwable ex) throws Throwable {
            if (ex instanceof InvocationTargetException) {
                throw ex.getCause();
            }
            throw ex;
        }
    };

    public MethodInvokerTemplate(final MethodSignature sig) {
        this.sig = sig;
    }

    /**
     * @inheritDoc
     */
    @Override
    public MethodSignature getMethodSignature() {
        return sig;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ExceptionHandlingPolicy getExceptionHandlingPolicy() {
        return policy;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setExceptionHandlerPolicy(final ExceptionHandlingPolicy exceptionHandlerPolicy) {
        this.policy = exceptionHandlerPolicy;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setTypeConverter(final TypeConverter converter) {
        this.converter = converter;
    }

    /**
     * @inheritDoc
     */
    @Override
    public final Object handleInvocation(final Object delegate, final Object[] params) throws Throwable {
        try {
            final Method method = getMethodBySignature(delegate.getClass(), getMethodSignature());
            if (method == null) {
                throw new NoSuchMethodException(String.format("Method %s was not found.", sig.getName()));
            }
            final CallSite site = beforeInvocation(delegate, method, params);
            final Object returnValue = site.dispatch();
            if (converter != null) {
                //noinspection unchecked
                return afterInvocation(converter.convert(returnValue), site);
            }
            return afterInvocation(returnValue, site);
        } catch (Throwable e) {
            return getExceptionHandlingPolicy().handleException(e);
        }
    }

    /**
     * Called immediately before invocation, this method <b>must</b> generate a
     * {@link org.nebularis.defproxy.introspection.CallSite} for the invocation, whose
     * return value (i.e., the result of calling {@link org.nebularis.defproxy.introspection.CallSite#dispatch()})
     * will be passed to {@link MethodInvokerTemplate#afterInvocation(Object, org.nebularis.defproxy.introspection.CallSite)}
     * for post-processing prior to returning it to the caller.
     * @param delegate
     * @param method
     * @param params
     * @return
     */
    protected CallSite beforeInvocation(final Object delegate, final Method method, final Object[] params) {
        return new CallSite(method, delegate, params);
    }

    /**
     * Called immediately after invocation, allowing for post-processing of the return value.
     * @param returnValue
     * @param site
     * @return
     */
    protected Object afterInvocation(final Object returnValue, final CallSite site) {
        return returnValue;
    }

    /**
     * Gets the correct method from the supplied class using the supplied method signature.
     * @param delegate
     * @param sig
     * @return
     */
    /* oh for the CLR's assembly protection level! */
    Method getMethodBySignature(final Class delegate, final MethodSignature sig) {
        return getMatchingAccessibleMethod(delegate, sig.getName(), sig.getParameterTypes());
    }
}

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
package org.nebularis.defproxy;

import org.nebularis.defproxy.support.CallSite;
import org.nebularis.defproxy.support.ExceptionHandlingPolicy;
import org.nebularis.defproxy.support.MethodInvoker;
import org.nebularis.defproxy.support.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.apache.commons.beanutils.MethodUtils.getMatchingAccessibleMethod;

/**
 * Default, reflection based {@link org.nebularis.defproxy.support.MethodInvoker},
 * providing additional support for template methods.
 */
class DefaultMethodInvoker implements MethodInvoker {

    private final MethodSignature sig;
    private ExceptionHandlingPolicy policy = new ExceptionHandlingPolicy() {
        @Override
        public Object handleException(final Throwable ex) throws Throwable {
            if (ex instanceof InvocationTargetException) {
                throw ex.getCause();
            }
            throw ex;
        }
    };

    public DefaultMethodInvoker(final MethodSignature sig) {
        this.sig = sig;
    }

    public MethodSignature getMethodSignature() {
        return sig;
    }

    public ExceptionHandlingPolicy getExceptionHandlingPolicy() {
        return policy;
    }

    public void setExceptionHandlerPolicy(final ExceptionHandlingPolicy exceptionHandlerPolicy) {
        this.policy = exceptionHandlerPolicy;
    }

    @Override
    public Object handleInvocation(final Object delegate, final Object[] params) throws Throwable {
        try {
            final Method method = getMethodBySignature(delegate.getClass(), sig);
            if (method == null) {
                throw new NoSuchMethodException(String.format("Method %s was not found.", sig.getName()));
            }
            final CallSite site = beforeInvocation(delegate, method, params);
            final Object returnValue = site.dispatch();
            return afterInvocation(returnValue, delegate, method, params);
        } catch (Throwable e) {
            return policy.handleException(e);
        }
    }

    protected CallSite beforeInvocation(final Object delegate, final Method method, final Object[] params) {
        return new CallSite(method, delegate, params);
    }

    protected Object afterInvocation(final Object returnValue, final Object delegate, final Method method, final Object[] params) {
        return returnValue;
    }

    /**
     * Gets the correct method from the supplied class using the supplied method signature.
     * @param delegate
     * @param sig
     * @return
     */
    protected Method getMethodBySignature(final Class delegate, final MethodSignature sig) {
        return getMatchingAccessibleMethod(delegate, sig.getName(), sig.getParameterTypes());
    }
}

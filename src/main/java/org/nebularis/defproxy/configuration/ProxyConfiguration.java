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
package org.nebularis.defproxy.configuration;

import org.nebularis.defproxy.configuration.MethodInvocationNotSupportedException;
import org.nebularis.defproxy.introspection.MethodInvokerTemplate;
import org.nebularis.defproxy.introspection.MethodInvoker;
import org.nebularis.defproxy.introspection.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the mapping between a {@link org.nebularis.defproxy.introspection.MethodSignature}
 * on a proxy interface and a {@link org.nebularis.defproxy.introspection.MethodInvoker} that
 * handles calling the delegate object behind the interface.
 */
public class ProxyConfiguration {

    private final Map<MethodSignature, MethodInvoker> cache = new HashMap<MethodSignature, MethodInvoker>();

    /**
     * Registered a {@link org.nebularis.defproxy.introspection.MethodInvokerTemplate} to handle
     * calls to <code>method</code>.
     * @param method the method to register handling for
     */
    void registerMethodInvoker(final Method method) {
        final MethodSignature sig = MethodSignature.fromMethod(method);
        registerMethodInvoker(sig, new MethodInvokerTemplate(sig));
    }

    /**
     * Registered a {@link org.nebularis.defproxy.introspection.MethodInvoker} to handle
     * calls to <code>method</code>.
     * @param method the method to register handling for
     * @param mi the {@link org.nebularis.defproxy.introspection.MethodInvoker} to register
     */
    void registerMethodInvoker(final Method method, final MethodInvoker mi) {
        registerMethodInvoker(MethodSignature.fromMethod(method), mi);
    }

    /**
     * Registered a {@link org.nebularis.defproxy.introspection.MethodInvoker} to handle
     * calls to <code>sig</code>. 
     * @param sig the {@link org.nebularis.defproxy.introspection.MethodSignature} to map it to.
     * @param mi the {@link org.nebularis.defproxy.introspection.MethodInvoker} to register
     */
    void registerMethodInvoker(final MethodSignature sig, final MethodInvoker mi) {
        cache.put(sig, mi);
    }

    /**
     * Gets a {@link org.nebularis.defproxy.introspection.MethodInvoker} for the supplied
     * {@link java.lang.reflect.Method}, using the type signature to find an appropriate instance.
     * @param method the method whose type signature you wish to use when looking up an invoker
     * @return a {@link org.nebularis.defproxy.introspection.MethodInvoker} instance.
     * @throws MethodInvocationNotSupportedException if no invoker has been registered against this <code>method</code>
     */
    public MethodInvoker getMethodInvoker(final Method method) throws MethodInvocationNotSupportedException {
        final MethodSignature sig = MethodSignature.fromMethod(method);
        final MethodInvoker invoker = cache.get(sig);
        if (invoker == null) {
            throw new MethodInvocationNotSupportedException(method);
        }
        return invoker;
    }

}

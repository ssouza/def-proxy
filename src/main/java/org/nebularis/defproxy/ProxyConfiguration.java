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

import org.nebularis.defproxy.support.MethodInvoker;
import org.nebularis.defproxy.support.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

// Q: do I just store mappings between method sigs and invokers?
// Q: or do I also provide factory methods for looking up the correct invoker!?

/**
 * 
 */
public class ProxyConfiguration {

    private final Map<MethodSignature, MethodInvoker> cache = new HashMap<MethodSignature, MethodInvoker>();

    /**
     * Registered a {@link org.nebularis.defproxy.DefaultMethodInvoker} to handle
     * calls to <code>method</code>.
     * @param method the method to register handling for
     */
    void registerMethodInvoker(final Method method) {
        final MethodSignature sig = MethodSignature.fromMethod(method);
        registerMethodInvoker(new DefaultMethodInvoker(sig), sig);
    }

    /**
     * Registered a {@link org.nebularis.defproxy.support.MethodInvoker} to handle
     * calls to <code>method</code>.
     * @param mi the {@link org.nebularis.defproxy.support.MethodInvoker} to register
     * @param method the method to register handling for
     */
    void registerMethodInvoker(final MethodInvoker mi, final Method method) {
        registerMethodInvoker(mi, MethodSignature.fromMethod(method));
    }

    /**
     * Registered a {@link org.nebularis.defproxy.support.MethodInvoker} to handle
     * calls to <code>sig</code>. 
     * @param mi the {@link org.nebularis.defproxy.support.MethodInvoker} to register
     * @param sig the {@link org.nebularis.defproxy.support.MethodSignature} to map it to.
     */
    void registerMethodInvoker(final MethodInvoker mi, final MethodSignature sig) {
        cache.put(sig, mi);
    }

    /**
     * Gets a {@link org.nebularis.defproxy.support.MethodInvoker} for the supplied
     * {@link java.lang.reflect.Method}, using the type signature to find an appropriate instance.
     * @param method the method whose type signature you wish to use when looking up an invoker
     * @return a {@link org.nebularis.defproxy.support.MethodInvoker} instance.
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

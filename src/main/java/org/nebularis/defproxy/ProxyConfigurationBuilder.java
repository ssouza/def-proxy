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

import org.apache.commons.lang.Validate;
import org.nebularis.defproxy.support.MethodSignature;
import org.nebularis.defproxy.support.MethodSignatureValidator;

import java.util.HashMap;
import java.util.Map;

import static org.nebularis.defproxy.support.ReflectionUtils.isAssignable;

/**
 * Builder for proxy handler configurations.
 */
public class ProxyConfigurationBuilder {

    private final Class<?> interfaceClass;
    private final MethodSignatureValidator interfaceValidator;

    private final Class<?> delegateClass;
    private final MethodSignatureValidator delegateValidator;

    private final Map<MethodSignature, MethodSignature> directMappings =
            new HashMap<MethodSignature, MethodSignature>();

    public ProxyConfigurationBuilder(final Class<?> interfaceClass, final Class<?> delegateClass) {
        Validate.notNull(interfaceClass, "Interface Class cannot be null");
        Validate.notNull(delegateClass, "Delegate Class cannot be null");
        this.interfaceClass = interfaceClass;
        this.delegateClass = delegateClass;
        interfaceValidator = new MethodSignatureValidator(interfaceClass);
        delegateValidator = new MethodSignatureValidator(delegateClass);
    }

    /**
     * Gets the delegated {@link org.nebularis.defproxy.support.MethodSignature} for
     * the supplied interface method.
     * @param interfaceMethod
     * @return
     */
    public MethodSignature getDelegatedMethod(final MethodSignature interfaceMethod) {
        return directMappings.get(interfaceMethod);
    }

    /**
     * Delegates calls to the supplied {@link org.nebularis.defproxy.support.MethodSignature}
     * directly back to the delegate type, using the exact method signature supplied.
     * @param interfaceMethod
     */
    public void delegateMethod(final MethodSignature interfaceMethod) {
        delegateMethod(interfaceMethod, new MethodSignature(interfaceMethod));
    }

    /**
     * Delegates calls to the supplied {@link org.nebularis.defproxy.support.MethodSignature}
     * directly back to the delegate type, using the mapped method name, and the return type
     * and parameter types of the interface method signature supplied.
     * @param interfaceMethod
     * @param mappedMethodName
     */
    public void delegateViaMethod(final MethodSignature interfaceMethod, final String mappedMethodName) {
        delegateMethod(interfaceMethod, new MethodSignature(interfaceMethod.getReturnType(),
                mappedMethodName, interfaceMethod.getParameterTypes()));
    }

    /**
     * Delegates calls to the interface method, directly to the supplied delegate method.
     * @param interfaceMethod
     * @param delegateMethod
     */
    public void delegateMethod(final MethodSignature interfaceMethod, final MethodSignature delegateMethod) {
        Validate.notNull(interfaceMethod, "Interface method cannot be null");
        Validate.notNull(delegateMethod, "Delegate method cannot be null");
        directMappings.put(interfaceMethod, delegateMethod);
    }

    /**
     * Generates a {@link org.nebularis.defproxy.ProxyConfiguration} for the current
     * builder state, throwing a checked exception if the mapping is in any way incorrect.
     * @return
     * @throws MappingException
     */
    public ProxyConfiguration generateHandlerConfiguration() throws MappingException {
        for (Map.Entry<MethodSignature, MethodSignature> entry : directMappings.entrySet()) {
            final MethodSignature interfaceMethod = entry.getKey();
            final MethodSignature delegateMethod = entry.getValue();
            check(interfaceMethod, interfaceValidator, interfaceClass);
            check(delegateMethod, delegateValidator, delegateClass);
            checkCompatibility(interfaceMethod, delegateMethod);
        }
        return new ProxyConfiguration();
    }

    private void check(final MethodSignature sig, final MethodSignatureValidator validator, final Class<?> checkClass) throws InvalidMethodMappingException {
        if (!validator.check(sig)) {
            throw new InvalidMethodMappingException(sig, checkClass);
        }
    }

    static void checkCompatibility(MethodSignature interfaceMethod, MethodSignature delegateMethod) throws IncompatibleMethodMappingException {
        if (isAssignable(interfaceMethod.getReturnType(), delegateMethod.getReturnType())) {
           if (isAssignable(interfaceMethod.getParameterTypes(), delegateMethod.getParameterTypes())) {
               return;
           }
        }
        throw new IncompatibleMethodMappingException(interfaceMethod, delegateMethod);
    }
}

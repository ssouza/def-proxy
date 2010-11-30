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
package org.nebularis.defproxy.validation;

import org.nebularis.defproxy.introspection.MethodSignature;

import java.lang.reflect.Method;

import static org.apache.commons.beanutils.MethodUtils.getMatchingAccessibleMethod;
import static org.nebularis.defproxy.utils.ReflectionUtils.isAssignable;

/**
 * Provides a boolean check to determine whether or not
 * a {@link org.nebularis.defproxy.introspection.MethodSignature} is valid
 * for the type provided upon instantiation.
 */
public class MethodSignatureValidator {

    private final Class<?> delegateClass;

    public MethodSignatureValidator(final Class<?> delegateClass) {
        this.delegateClass = delegateClass;
    }

    /**
     * Check that the delegateClass underlying this instance has
     * a method matching the supplied name (assuming no arguments
     * and a return type of void).
     * @param methodName
     * @return
     */
    public boolean check(final String methodName) {
        // assume that the return type is void
        // assume that the input parameter type array is empty
        return check(void.class, methodName);
    }

    /**
     * Check that the delegateClass underlying this instance has
     * a method matching the supplied name and argument types
     * (assuming a return type of void).
     * @param methodName
     * @param inputTypes
     * @return
     */
    public boolean check(final String methodName, Class... inputTypes) {
        return check(void.class, methodName, inputTypes);
    }

    /**
     * Check that the delegateClass underlying this instance has
     * a method matching the supplied {@link org.nebularis.defproxy.introspection.MethodSignature}.
     * @param sig
     * @return
     */
    public boolean check(final MethodSignature sig) {
        return check(sig.getReturnType(), sig.getName(), sig.getParameterTypes());
    }

    /**
     * Check that the delegateClass underlying this instance has
     * a method mathcing the supplied name, argument list and return types.
     * @param returnType
     * @param methodName
     * @param inputTypes
     * @return
     */
    public boolean check(final Class<?> returnType, final String methodName, final Class... inputTypes) {
        final Method method = getMatchingAccessibleMethod(delegateClass, methodName, inputTypes);
        return (method != null && isAssignable(returnType, method.getReturnType()));
    }

}

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
package org.nebularis.defproxy.support;

import org.apache.commons.lang.ClassUtils;

import java.lang.reflect.Method;

import static org.apache.commons.beanutils.MethodUtils.getMatchingAccessibleMethod;

public class MethodSignatureValidator {

    private final Class<?> delegateClass;

    public MethodSignatureValidator(final Class<?> delegateClass) {
        this.delegateClass = delegateClass;
    }

    public boolean check(final String methodName) {
        // assume that the return type is void
        // assume that the input parameter type array is empty
        return check(void.class, methodName);
    }

    public boolean check(final String methodName, Class... inputTypes) {
        return check(void.class, methodName, inputTypes);
    }

    public boolean check(final MethodSignature sig) {
        return check(sig.getReturnType(), sig.getName(), sig.getParameterTypes());
    }

    public boolean check(final Class<?> returnType, final String methodName, final Class... inputTypes) {
        final Method method = getMatchingAccessibleMethod(delegateClass, methodName, inputTypes);
        return (method != null && isAssignable(returnType, method.getReturnType()));
    }

    private boolean isAssignable(final Class<?> expectedReturnType, final Class<?> methodReturnType) {
        final boolean straightCheck = ClassUtils.isAssignable(methodReturnType, expectedReturnType);
        if (!straightCheck) {
            if (methodReturnType.isPrimitive()) {
                try {
                    final Class metaType = (Class) expectedReturnType.getField("TYPE").get(expectedReturnType);
                    return ClassUtils.isAssignable(methodReturnType, metaType);
                } catch (Exception e) {
                    return false;
                } 
            }
        }
        return straightCheck;
    }

}

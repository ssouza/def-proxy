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

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.reflect.Method;

/**
 * Method signature (not calling convention).
 */
public final class MethodSignature {
    private final Class<?> returnType;
    private final String name;
    private final Class<?>[] parameterTypes;

    public MethodSignature(final Class<?> returnType, final String name, final Class<?> ... parameterTypes) {
        Validate.notNull(returnType, "return type cannot be null");
        Validate.notEmpty(name, "method name cannot be null");
        Validate.noNullElements(parameterTypes, "parameter types cannot contain null elements");
        this.returnType = returnType;
        this.name = name;
        this.parameterTypes = parameterTypes;
    }

    /**
     * Copy constructor
     * @param sig
     */
    public MethodSignature(final MethodSignature sig) {
        this(sig.getReturnType(), sig.getName(), sig.getParameterTypes());
    }

    public static MethodSignature fromMethod(final Method method) {
        Validate.notNull(method, "method cannot be null");
        return new MethodSignature(method.getReturnType(), method.getName(), method.getParameterTypes());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(returnType)
                .append(name)
                .append(parameterTypes)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object o) {
        return (o instanceof MethodSignature) && equals((MethodSignature)o);
    }

    public boolean equals(final MethodSignature ms) {
        return ms != null && new EqualsBuilder()
                .append(returnType, ms.returnType)
                .append(name, ms.name)
                .append(parameterTypes, ms.parameterTypes)
                .isEquals();
    }

    public Class getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }
}

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

package org.nebularis.defproxy.introspection;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.reflect.Method;

import static org.nebularis.defproxy.introspection.ReflectionUtils.isAssignable;

/**
 * Represents a method signature (though not its calling convention).
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

    /**
     * Instantiates a {@link org.nebularis.defproxy.introspection.MethodSignature}
     * from a {@link java.lang.reflect.Method} object.
     * @param method
     * @return
     */
    public static MethodSignature fromMethod(final Method method) {
        Validate.notNull(method, "method cannot be null");
        return new MethodSignature(method.getReturnType(), method.getName(), method.getParameterTypes());
    }

    /**
     * Returns <code>true</code> if this {@link org.nebularis.defproxy.introspection.MethodSignature}
     * is compatible in its {@link org.nebularis.defproxy.introspection.MethodSignature#returnType} and
     * {@link org.nebularis.defproxy.introspection.MethodSignature#parameterTypes} with the other supplied.
     * @param other the {@link org.nebularis.defproxy.introspection.MethodSignature} with which to compare this instance.
     * @return <code>true</code> if this instance is compatible with <code>other</code>, otherwise <code>false</code>.
     */
    public boolean isCompatibleWith(final MethodSignature other) {
        if (isAssignable(getReturnType(), other.getReturnType())) {
            return isAssignable(getParameterTypes(), other.getParameterTypes());
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(returnType)
                .append(name)
                .append(parameterTypes)
                .toHashCode();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(final Object o) {
        return (o instanceof MethodSignature) && equals((MethodSignature)o);
    }

    /**
     * @inheritDoc
     */
    public boolean equals(final MethodSignature ms) {
        return ms != null && new EqualsBuilder()
                .append(returnType, ms.returnType)
                .append(name, ms.name)
                .append(parameterTypes, ms.parameterTypes)
                .isEquals();
    }

    /**
     * Gets the return type defined by this {@link org.nebularis.defproxy.introspection.MethodSignature}.
     * @return
     */
    public Class getReturnType() {
        return returnType;
    }

    /**
     * Gets the method name defined by this {@link org.nebularis.defproxy.introspection.MethodSignature}.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the types of the formal parameter list defined by this {@link org.nebularis.defproxy.introspection.MethodSignature}.
     * @return
     */
    public Class[] getParameterTypes() {
        return parameterTypes;
    }
}

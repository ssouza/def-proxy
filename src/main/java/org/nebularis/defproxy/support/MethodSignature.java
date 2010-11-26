package org.nebularis.defproxy.support;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.reflect.Method;

/**
 * Method signature (not calling convention).
 */
public class MethodSignature {
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

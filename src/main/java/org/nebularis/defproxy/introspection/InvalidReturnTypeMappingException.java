package org.nebularis.defproxy.introspection;

public class InvalidReturnTypeMappingException extends InvalidMethodMappingException {
    public InvalidReturnTypeMappingException(final MethodSignature sig, final Class<?> targetType) {
        super(sig, targetType);
    }
}

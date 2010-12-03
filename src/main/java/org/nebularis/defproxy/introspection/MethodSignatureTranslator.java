package org.nebularis.defproxy.introspection;

import org.apache.commons.lang.Validate;

/**
 * Encapsulates the transition between a method declared on a proxy interface
 * and a target site mapped onto a delegate object.
 */
public class MethodSignatureTranslator {

    private MethodSignature interfaceMethod;
    private Class<?> interfaceType;
    private Class<?> delegateType;

    public MethodSignatureTranslator(final MethodSignature interfaceMethod,
                                     final Class<?> interfaceType, final Class<?> delegateType) {
        Validate.notNull(interfaceMethod, "Interface method cannot be null.");
        Validate.notNull(interfaceType, "Interface type cannot be null.");
        Validate.notNull(delegateType, "Delegate type cannot be null.");
        this.interfaceMethod = interfaceMethod;
        this.interfaceType = interfaceType;
        this.delegateType = delegateType;
    }

    public void verifyMethodSignatures() throws MappingException {
        // interfaceMethod.resolveMethodSignature(interfaceType);
        throw new InvalidMethodMappingException(interfaceMethod, interfaceType);
    }

    public MethodSignature getInterfaceMethod() {
        return interfaceMethod;
    }

    public Class<?> getInterfaceType() {
        return interfaceType;
    }

    public MethodSignature getDelegateMethod() {
        return new MethodSignature(interfaceMethod);
    }

    public Class<?> getDelegateType() {
        return delegateType;
    }
}

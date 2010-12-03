package org.nebularis.defproxy.introspection;

import org.apache.commons.lang.Validate;

/**
 * Encapsulates the transition between a method declared on a proxy interface
 * and a target site mapped onto a delegate object.
 */
public class MethodSignatureTranslator {

    private MethodSignature interfaceMethod;
    private Class<?> interfaceType;

    public MethodSignatureTranslator(final MethodSignature interfaceMethod,
                                     final Class<?> interfaceType, final Class<?> delegateType) {
        Validate.notNull(interfaceMethod, "Interface method cannot be null.");
        Validate.notNull(interfaceType, "Interface type cannot be null.");
        Validate.notNull(delegateType, "Delegate type cannot be null.");
        this.interfaceMethod = interfaceMethod;
        this.interfaceType = interfaceType;
    }

    public void verifyMethodSignatures() throws MappingException {
        throw new InvalidMethodMappingException(interfaceMethod, interfaceType);
    }

    public MethodSignature getInterfaceMethod() {
        return interfaceMethod;
    }

    public Class<?> getInterfaceType() {
        return interfaceType;
    }
}

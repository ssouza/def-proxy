package org.nebularis.defproxy.introspection;

import org.apache.commons.lang.Validate;

import java.lang.reflect.Method;

import static org.nebularis.defproxy.introspection.ReflectionUtils.isAssignable;

/**
 * Encapsulates the transition between a method declared on a proxy interface
 * and a target site mapped onto a delegate object.
 */
public class MethodSignatureTranslator {

    private final MethodSignature interfaceMethod;
    private final Class<?> interfaceType;
    private final Class<?> delegateType;
    private String delegateMethodNameOverride;

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
        /*final Method originCallee = */
        interfaceMethod.resolveToMethod(interfaceType);
        final MethodSignature delegateMethod = getDelegateMethod();

        final Method delegateTargetSite = delegateMethod.resolveToMethod(delegateType);
        if (!isAssignable(delegateTargetSite.getReturnType(), interfaceMethod.getReturnType())) {
            throw new IncompatibleMethodMappingException(interfaceMethod, delegateMethod);
        }
    }

    public MethodSignature getInterfaceMethod() {
        return interfaceMethod;
    }

    public Class<?> getInterfaceType() {
        return interfaceType;
    }

    public MethodSignature getDelegateMethod() {
        // TODO: examine the tuple (typeConv, proxyAdditionalArgs, altName) and generate the appropriate method signature

        // some (most ?) of these calculations currently live in the proxy configuration builder so move tests + code

        /*
        // final TypeConverter typeConverter = getTypeConverterIfAvailable();
        // AdditionalArguments additionalParams;
        final Class<?> returnType = figureOutTheReturnType();
        final Class[] parameterTypes = calculateParameterTypes();
        return new MethodSignature(returnType, name, parameterTypes);*/

        final String name = (delegateMethodNameOverride != null) ?
                    delegateMethodNameOverride : interfaceMethod.getName();
        return new MethodSignature(interfaceMethod.getReturnType(), name, interfaceMethod.getParameterTypes());
    }

    public Class<?> getDelegateType() {
        return delegateType;
    }

    public String getDelegateMethodNameOverride() {
        return delegateMethodNameOverride;
    }

    public void setDelegateMethodNameOverride(final String delegateMethodNameOverride) {
        this.delegateMethodNameOverride = delegateMethodNameOverride;
    }
}

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

package org.nebularis.defproxy.configuration;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.Validate;
import org.nebularis.defproxy.annotations.Insertion;
import org.nebularis.defproxy.configuration.IncompatibleMethodMappingException;
import org.nebularis.defproxy.configuration.InvalidMethodMappingException;
import org.nebularis.defproxy.configuration.MappingException;
import org.nebularis.defproxy.configuration.ProxyConfiguration;
import org.nebularis.defproxy.introspection.*;
import org.nebularis.defproxy.validation.MethodSignatureValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.nebularis.defproxy.introspection.ReflectionUtils.isAssignable;

/**
 * Builder for proxy handler configurations. You use this class to
 * configure the wiring between your proxy interface and backing/delegate
 * object types.
 * <p/>
 * The framework supports direct mappings based on method signature,
 * variations in name, arity and argument/return types as well as
 * custom exception handling policies.
 */
public class ProxyConfigurationBuilder {

    private static class WrapperSlot {
        public final Insertion insertion;
        public final Object[] params;

        public WrapperSlot(final Insertion insertion, final Object[] params) {
            this.insertion = insertion;
            this.params = params;
        }
    }

    private final Class<?> interfaceClass;
    private MethodSignatureValidator interfaceValidator;

    private final Class<?> delegateClass;
    private MethodSignatureValidator delegateValidator;

    private TypeConverterFactory converterFactory;

    private final Map<MethodSignature, MethodSignature> directMappings = new HashMap<MethodSignature, MethodSignature>();
    private final Map<MethodSignature, TypeConverter> conversionMappings = new HashMap<MethodSignature, TypeConverter>();
    private final Map<MethodSignature, WrapperSlot> targetSiteWrappers = new HashMap<MethodSignature, WrapperSlot>();

    public ProxyConfigurationBuilder(final Class<?> interfaceClass, final Class<?> delegateClass) {
        Validate.notNull(interfaceClass, "Interface Class cannot be null");
        Validate.notNull(delegateClass, "Delegate Class cannot be null");
        this.interfaceClass = interfaceClass;
        this.delegateClass = delegateClass;
        setInterfaceValidator(new MethodSignatureValidator(interfaceClass));
        setDelegateValidator(new MethodSignatureValidator(delegateClass));
    }

    public TypeConverterFactory getTypeConverterFactory() {
        return converterFactory;
    }

    public void setTypeConverterFactory(final TypeConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    public MethodSignatureValidator getInterfaceValidator() {
        return interfaceValidator;
    }

    public void setInterfaceValidator(final MethodSignatureValidator interfaceValidator) {
        this.interfaceValidator = interfaceValidator;
    }

    public MethodSignatureValidator getDelegateValidator() {
        return delegateValidator;
    }

    public void setDelegateValidator(final MethodSignatureValidator delegateValidator) {
        this.delegateValidator = delegateValidator;
    }

    /**
     * Gets the delegated {@link org.nebularis.defproxy.introspection.MethodSignature} for
     * the supplied interface method.
     *
     * @param interfaceMethod
     * @return
     */
    public MethodSignature getDelegatedMethod(final MethodSignature interfaceMethod) {
        return directMappings.get(interfaceMethod);
    }

    /**
     * Delegates calls to the supplied {@link org.nebularis.defproxy.introspection.MethodSignature}
     * directly back to the delegate type, using the exact method signature supplied.
     *
     * @param interfaceMethod
     */
    public void delegateMethod(final MethodSignature interfaceMethod) {
        delegateMethod(interfaceMethod, new MethodSignature(interfaceMethod));
    }

    /**
     * Delegates calls to the supplied {@link org.nebularis.defproxy.introspection.MethodSignature}
     * directly back to the delegate type, using the mapped method name, and the return type
     * and parameter types of the interface method signature supplied.
     *
     * @param interfaceMethod
     * @param mappedMethodName
     */
    public void delegateViaMethod(final MethodSignature interfaceMethod, final String mappedMethodName) {
        delegateMethod(interfaceMethod, new MethodSignature(interfaceMethod.getReturnType(),
                mappedMethodName, interfaceMethod.getParameterTypes()));
    }

    /**
     * Delegates calls to the interface method, directly to the supplied delegate method.
     *
     * @param interfaceMethod
     * @param delegateMethod
     */
    public void delegateMethod(final MethodSignature interfaceMethod, final MethodSignature delegateMethod) {
        Validate.notNull(interfaceMethod, "Interface method cannot be null");
        Validate.notNull(delegateMethod, "Delegate method cannot be null");
        directMappings.put(interfaceMethod, delegateMethod);
    }

    /**
     * Set a {@link org.nebularis.defproxy.introspection.TypeConverter} for the provided interface method.
     * Providing a converter means that the return type of the delegate method can differ from
     * that of the underlying delegate, providing that the type converter can massage values from one to the other.
     *
     * @param interfaceMethod
     * @param converter
     */
    public void setTypeConverter(final MethodSignature interfaceMethod, final TypeConverter converter) {
        conversionMappings.put(interfaceMethod, converter);
    }

    /**
     * Wrap calls to the supplied delegateMethod with the specified (additional) parameters.
     *
     * @param prefix
     * @param delegateMethod
     * @param params
     */
    public void wrapDelegate(final Insertion prefix, final MethodSignature delegateMethod, final Object... params) {
        targetSiteWrappers.put(delegateMethod, new WrapperSlot(prefix, params));
    }

    /**
     * Generates a {@link org.nebularis.defproxy.configuration.ProxyConfiguration} for the current
     * builder state, throwing a checked exception if the mapping is in any way incorrect.
     *
     * @return
     * @throws MappingException
     */
    public ProxyConfiguration generateProxyConfiguration() throws MappingException {
        final ProxyConfiguration configuration = new ProxyConfiguration();
        for (Map.Entry<MethodSignature, MethodSignature> entry : directMappings.entrySet()) {
            final MethodSignature interfaceMethod = entry.getKey();
            final MethodSignature delegateMethod = entry.getValue();
            check(interfaceMethod, getInterfaceValidator(), interfaceClass);
            check(delegateMethod, getDelegateValidator(), delegateClass);
            checkCompatibility(interfaceMethod, delegateMethod);

            MethodInvoker invoker;
            if (targetSiteWrappers.containsKey(delegateMethod)) {
                final WrapperSlot slot = targetSiteWrappers.get(delegateMethod);
                invoker = new TargetSiteWrapper(delegateMethod, slot.insertion, slot.params);
            } else {
                invoker = new MethodInvokerTemplate(delegateMethod);
            }

            invoker.setTypeConverter(getTypeConverter(interfaceMethod, delegateMethod));
            configuration.registerMethodInvoker(interfaceMethod, invoker);
        }
        return configuration;
    }

    private void check(final MethodSignature sig, final MethodSignatureValidator validator, final Class<?> checkClass) throws InvalidMethodMappingException {
        if (!validator.check(sig)) {
            throw new InvalidMethodMappingException(sig, checkClass);
        }
    }

    public void checkCompatibility(MethodSignature interfaceMethod, MethodSignature delegateMethod) throws IncompatibleMethodMappingException {
        if (returnTypesAreCompatible(interfaceMethod, delegateMethod)) {
            if (areParameterTypesCompatible(interfaceMethod, delegateMethod)) {
                return;
            }
        }
        throw new IncompatibleMethodMappingException(interfaceMethod, delegateMethod);
    }

    private boolean returnTypesAreCompatible(final MethodSignature interfaceMethod, final MethodSignature delegateMethod) {
        final TypeConverter converter = getTypeConverter(interfaceMethod, delegateMethod);
        if (converter != null && isAssignable(delegateMethod.getReturnType(), converter.getInputType())) {
            return isAssignable(interfaceMethod.getReturnType(), converter.getOutputType());
        }
        return isAssignable(interfaceMethod.getReturnType(), delegateMethod.getReturnType());
    }

    private TypeConverter getTypeConverter(final MethodSignature interfaceMethod, final MethodSignature delegateMethod) {
        TypeConverter converter = null;
        if (conversionMappings.containsKey(interfaceMethod)) {
            converter = conversionMappings.get(interfaceMethod);
        } else if (converterFactory != null) {
            converter = converterFactory.createTypeConverter(delegateMethod.getReturnType(), interfaceMethod.getReturnType());
            if (converter != null) {
                setTypeConverter(interfaceMethod, converter);
            }
        }
        return converter;
    }

    private boolean areParameterTypesCompatible(final MethodSignature interfaceMethod, final MethodSignature delegateMethod) {
        if (targetSiteWrappers.containsKey(delegateMethod)) {
            final WrapperSlot slot = targetSiteWrappers.get(delegateMethod);
            if (slot.insertion.equals(Insertion.Prefix)) {
                if (slot.params.length < delegateMethod.getParameterTypes().length) {
                    final Class[] inputTypes = new Class[slot.params.length];
                    final List<Class> inputClassList = asList(delegateMethod.getParameterTypes());
                    inputClassList.subList(0, inputTypes.length).toArray(inputTypes);
                    return isAssignable(inputTypes, ClassUtils.toClass(slot.params));
                }
                return isAssignable(delegateMethod.getParameterTypes(), ClassUtils.toClass(slot.params));
            } else {
                assert (slot.insertion.equals(Insertion.Suffix));
                final Class[] inputTypes = new Class[delegateMethod.getParameterTypes().length];
                final List<Class> inputClassList = asList(delegateMethod.getParameterTypes());
                inputClassList.toArray(inputTypes);
                CollectionUtils.reverseArray(inputTypes);

                final Class[] slotTypes = new Class[slot.params.length];
                final List<Class> slotClassList = asList(ClassUtils.toClass(slot.params));
                slotClassList.toArray(slotTypes);
                CollectionUtils.reverseArray(slotTypes);
                return isAssignable(inputTypes, slotTypes);
            }
        }
        return isAssignable(interfaceMethod.getParameterTypes(), delegateMethod.getParameterTypes());
    }

}

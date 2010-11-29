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
package org.nebularis.defproxy;

import org.apache.commons.lang.Validate;
import org.nebularis.defproxy.support.MethodSignature;
import org.nebularis.defproxy.support.MethodSignatureValidator;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang.ClassUtils.isAssignable;

/**
 * Builder for proxy handler configurations.
 */
public class ProxyConfigurationBuilder {

    private final Class<?> interfaceClass;
    private final MethodSignatureValidator interfaceValidator;

    private final Class<?> delegateClass;
    private final MethodSignatureValidator delegateValidator;

    private final Map<MethodSignature, MethodSignature> directMappings =
            new HashMap<MethodSignature, MethodSignature>();

    public ProxyConfigurationBuilder(final Class<?> interfaceClass, final Class<?> delegateClass) {
        Validate.notNull(interfaceClass, "Interface Class cannot be null");
        Validate.notNull(delegateClass, "Delegate Class cannot be null");
        this.interfaceClass = interfaceClass;
        this.delegateClass = delegateClass;
        interfaceValidator = new MethodSignatureValidator(interfaceClass);
        delegateValidator = new MethodSignatureValidator(delegateClass);
    }

    public void delegateMethod(final MethodSignature interfaceMethod, final MethodSignature delegateMethod) {
        Validate.notNull(interfaceMethod, "Interface method cannot be null");
        Validate.notNull(delegateMethod, "Delegate method cannot be null");
        directMappings.put(interfaceMethod, delegateMethod);
    }

    public ProxyConfiguration generateHandlerConfiguration() throws InvalidMethodMappingException {
        for (Map.Entry<MethodSignature, MethodSignature> entry : directMappings.entrySet()) {
            final MethodSignature interfaceMethod = entry.getKey();
            final MethodSignature delegateMethod = entry.getValue();
            check(interfaceMethod, interfaceValidator, interfaceClass);
            check(delegateMethod, delegateValidator, delegateClass);
        }
        return new ProxyConfiguration();
    }

    private void check(final MethodSignature sig, final MethodSignatureValidator validator, final Class<?> checkClass) throws InvalidMethodMappingException {
        if (!validator.check(sig)) {
            throw new InvalidMethodMappingException(sig, checkClass);
        }
    }

    static void checkCompatibility(MethodSignature interfaceMethod, MethodSignature delegateMethod) throws IncompatibleMethodMappingException {
        if (isAssignable(delegateMethod.getReturnType(), interfaceMethod.getReturnType())) {
           if (isAssignable(delegateMethod.getParameterTypes(), interfaceMethod.getParameterTypes())) {
               return;
           }
        }
        throw new IncompatibleMethodMappingException(interfaceMethod, delegateMethod);
    }
}

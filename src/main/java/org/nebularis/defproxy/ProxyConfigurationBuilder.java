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

/**
 * Builder for proxy handler configurations.
 */
public class ProxyConfigurationBuilder {

    private final Class<?> interfaceClass;
    private final Class<?> delegateClass;

    public ProxyConfigurationBuilder(final Class<?> interfaceClass, final Class<?> delegateClass) {
        Validate.notNull(interfaceClass, "Interface Class cannot be null");
        Validate.notNull(delegateClass, "Delegate Class cannot be null");
        this.interfaceClass = interfaceClass;
        this.delegateClass = delegateClass;
    }

    public ProxyConfiguration generateHandlerConfiguration() throws InvalidInterfaceMappingException {
        throw new InvalidInterfaceMappingException();
    }

    public void delegateMethod(final MethodSignature interfaceMethod, final MethodSignature delegateMethod) {
        Validate.notNull(interfaceMethod, "Interface method cannot be null");
        Validate.notNull(delegateMethod, "Delegate method cannot be null");
    }
}
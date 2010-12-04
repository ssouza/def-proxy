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

package org.nebularis.defproxy;

import org.nebularis.defproxy.annotations.ProxyTypeConverterFactory;

/**
 * Thrown when a proxies configuration (either programmatic or annotation
 * based) is in some way invalid.
 */
public class InvalidProxyConfigurationException extends RuntimeException {

    private final Object context;
    private final Class<?> proxyInterface;

    public InvalidProxyConfigurationException(final Object context, final Class<?> proxyInterface) {
        this(context, proxyInterface, "", null);
    }

    public InvalidProxyConfigurationException(final Object context, final Class<?> proxyInterface, final String message) {
        this(context, proxyInterface, message, null);
    }

    public InvalidProxyConfigurationException(final Object context, final Class<?> proxyInterface,
                                              final String message, final Throwable ex) {
        super(message, ex);
        this.context = context;
        this.proxyInterface = proxyInterface;
    }

    /**
     * The context of the configuration failure (e.g., the delegate, an incorrectly mapped annotation, etc).
     * @return
     */
    public Object getContext() {
        return context;
    }

    public Class<?> getProxyInterface() {
        return proxyInterface;
    }
}

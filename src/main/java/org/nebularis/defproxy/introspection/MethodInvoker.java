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
package org.nebularis.defproxy.introspection;

import org.nebularis.defproxy.configuration.ExceptionHandlingPolicy;
import org.nebularis.defproxy.utils.TypeConverter;

import java.lang.reflect.Method;

/**
 * The external behavioural contract for custom method invocations.
 */
public interface MethodInvoker {
    MethodSignature getMethodSignature();

    ExceptionHandlingPolicy getExceptionHandlingPolicy();

    void setExceptionHandlerPolicy(ExceptionHandlingPolicy exceptionHandlerPolicy);

    void setTypeConverter(final TypeConverter converter);

    /**
     * Final Class that represents "no method invoker" in a
     * {@link org.nebularis.defproxy.annotations.ProxyDelegated} annotation instance.
     */
    public final class NoMethodInvoker {}

    /**
     * Handles the invocation of a wrapped method, called with the supplied
     * delegate and additional items making up the formal argument list.
     * @param delegate
     * @param objects
     * @return
     */
    Object handleInvocation(Object delegate, Object... objects) throws Throwable;
}

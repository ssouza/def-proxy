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

import org.apache.commons.beanutils.ConstructorUtils;

/**
 * Provides an interface that {@link org.nebularis.defproxy.introspection.MethodInvoker}s
 * use at runtime to handle boundary conditions. 
 */
public abstract class ExceptionHandlingPolicy {

    /**
     * Provides a standard exception handling policy that wraps and re-throws
     * any thrown exception in one of the supplied type.
     * @param wrapperClass
     * @return
     */
    public static ExceptionHandlingPolicy wrapExceptions(final Class<? extends Throwable> wrapperClass) {
        return new ExceptionHandlingPolicy() {
            @Override
            public Object handleException(final Throwable ex) throws Throwable {
                throw ((Throwable) ConstructorUtils.invokeConstructor(wrapperClass, ex));
            }
        };
    }

    /**
     * The heart of the policy: chooses to ignore the supplied exception,
     * re-throw it, or wrap it in another exception and and throw that instead.
     * @param ex
     * @return
     * @throws Throwable
     */
    public abstract Object handleException(final Throwable ex) throws Throwable;

}

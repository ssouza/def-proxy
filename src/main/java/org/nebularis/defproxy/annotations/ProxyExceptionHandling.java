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

package org.nebularis.defproxy.annotations;

import org.nebularis.defproxy.configuration.ExceptionHandlingPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates an exception handling policy is set, wrapping
 * all raised errors in an exception of the defined type.
 *
 * In order to make this work, the exception type you specify
 * in {@link ProxyExceptionHandling#wrapWith()} must define
 * a constructor that accepts a single {@link Throwable}
 * subclass instance.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ProxyExceptionHandling {

    /**
     * Wrap all exceptions in an instance of the specified type.
     * @return
     */
    Class<? extends Throwable> wrapWith();

}

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

import org.nebularis.defproxy.introspection.TypeConverterFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides a means for {@link org.nebularis.defproxy.annotations.ProxyMethod}
 * decorated methods to return a different type than that of the mapped method
 * to which they delegate at runtime. This is achieved by associating a
 * {@link org.nebularis.defproxy.introspection.TypeConverterFactory} with the delegated method.
 *
 * Unlike {@link org.nebularis.defproxy.annotations.ProxyTypeConverter}, the factory
 * can be specified as a class level annotation, thereby allowing you to provide one
 * implementation for all methods. The specified {@link ProxyTypeConverterFactory#provider()}
 * class <b>must</b> expose a no-argument public constructor.
 *
 * <pre>
 * &#064;ProxyInterface(delegate = HashMap.class)
 * &#064;ProxyTypeConverterFactory(provider = MyConverterFactory.class)
 * public interface Item {
 *
 *     &#064;ProxyDelegated(methodName = "get")
 *     &#064;ProxyArguments(value = {"product-id"}, direction = Prefix)
 *     int productId();
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ProxyTypeConverterFactory {
    /**
     * The provider class.
     * @return
     */
    Class<? extends TypeConverterFactory> provider();
}

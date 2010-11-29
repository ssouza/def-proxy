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
package org.nebularis.defproxy.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation explicitly marks a method as delegated, allowing
 * you to specify the underlying method name or provide an invocation handler class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProxyDelegated {

    /**
     * Optional. Indicates the method name in the back-end to
     * which calls should be delegated. Where overloads are present,
     * the method's type signature will be used to determine the
     * correct callee.
     *
     * Example:
     * <pre>
     *      &#064;ProxyInterface(delegate=Baz.class)
     *      public interface Foo {
     *
     *          // this method will be resolved to a compatible signature in Baz.class
     *          &#064;ProxyDelegated
     *          String bar(final String name, final int age);
     *
     *          // this method will be resolved to a method `String flobby(void)` in Baz.class
     *          &#064;ProxyDelegated(methodName="flobby")
     *          String clobber();
     *      }
     * </pre>
     *
     * <em>This value is ignored if the {@link ProxyDelegated#methodInvocationHandler()}
     * option has been set. Otherwise, this value is required.</em>
     * @return
     */
    String methodName() default "";

    /**
     * Optional. Indicates a type which, once instantiated, will handle the
     * calls to the back-end.
     *
     * Example:
     * <pre>
     *      &#064;ProxyInterface(delegate=Baz.class)
     *      public interface Foo {
     *
     *          // this method will be resolved to a compatible signature in Baz.class
     *          &#064;ProxyDelegated(methodInvocationHandler=MyClass.class)
     *          String bar(final String name, final int age);
     *
     *      }
     * </pre>
     *
     * <em>This value is ignored if the {@link ProxyDelegated#methodName()}
     * option has been set. Otherwise, this value is required.</em>
     * @return
     */
    Class<? extends MethodInvoker> methodInvocationHandler() default MethodInvoker.class;
}

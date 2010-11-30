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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides annotations for passing additional string arguments to a
 * {@link org.nebularis.defproxy.annotations.ProxyDelegated} method
 * in order to modify the parameter array during dynamic dispatch.
 *
 * In the following example, the <code>barcode</code> method is mapped
 * to an underlying delegate <code>get</code> which takes an argument 
 * naming an attribute you wish to retrieve.
 *
 * <pre>
 * &#064;ProxyInterface(delegate = HashMap.class)
 * public interface Item {
 *
 *      &#064;ProxyDelegated(methodName = "get")
 *      &#064;ProxyArguments(value = {"bar-code"})
 *      String barcode();
 *
 * }
 * </pre>
 *
 * The optional {@link ProxyArguments#direction()} property indicates the
 * end of the formal argument list, to which the additional parameters should
 * be added (i.e., prepend or append them). It's default value of
 * {@link Insertion#Prefix}
 * indicates that the additional arguments will be passed in front of any explicitly
 * provided arguments.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProxyArguments {

    /**
     * An array of string arguments to add to the passed parameter list.
     * @return
     */
    String[] value() default {};

    /**
     * The direction in which to fix additional arguments to the existing parameter list.
     * @return
     */
    Insertion direction() default Insertion.Prefix;
}

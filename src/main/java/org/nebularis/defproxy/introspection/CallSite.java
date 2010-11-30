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

package org.nebularis.defproxy.introspection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Simple representation of a "dispatch-able" call site. Subclasses of
 * {@link org.nebularis.defproxy.introspection.MethodInvoker} can opt to
 * alter the default {@link org.nebularis.defproxy.introspection.CallSite}
 * which represents the target site to which a proxy invocation will be
 * dispatched at runtime.
 *
 * <pre>
 * public class FooMethodInvoker extends MethodInvokerTemplate {
 *
 *   &#064;Override
 *   protected CallSite beforeInvocation(final Object delegate, final Method method, final Object[] params) {
 *      // alter the formal parameter list.....
 *      return new CallSite(method, delegate, mergeParams(params));
 *   }
 *
 * }
 * </pre>
 */
public class CallSite {

    private final Object callee;
    private final Method target;
    private final Object[] arguments;

    public CallSite(final Method target, final Object callee, final Object[] arguments) {
        this.target = target;
        this.callee = callee;
        this.arguments = arguments;
    }

    public Object dispatch() throws InvocationTargetException, IllegalAccessException {
        return target.invoke(callee, arguments);
    }
}

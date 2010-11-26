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

import org.nebularis.defproxy.support.MethodInvoker;
import org.nebularis.defproxy.support.MethodSignature;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.apache.commons.beanutils.MethodUtils.getMatchingAccessibleMethod;

/**
 * Default, reflection based {@link org.nebularis.defproxy.support.MethodInvoker}.
 */
class DefaultMethodInvoker implements MethodInvoker {

    private final MethodSignature sig;

    public DefaultMethodInvoker(final MethodSignature sig) {
        this.sig = sig;
    }

    @Override
    public Object handleInvocation(final Object delegate, final Object[] objects) {
        final Method method = getAccessibleMethod(delegate.getClass());
        try {
            return method.invoke(delegate, objects);

            // TODO: proper exception handling policy
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Method getAccessibleMethod(final Class delegate) {
        return getMatchingAccessibleMethod(delegate, sig.getName(), sig.getParameterTypes());
    }
}

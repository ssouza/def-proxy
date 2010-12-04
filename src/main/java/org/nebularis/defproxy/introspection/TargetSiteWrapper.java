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

import org.nebularis.defproxy.annotations.Insertion;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 *
 */
public class TargetSiteWrapper extends MethodInvokerTemplate {
    private final Insertion insertion;
    private final Object[] additionalParams;

    public TargetSiteWrapper(final MethodSignature sig, final Insertion insertion, final Object... additionalParams) {
        super(sig);
        this.insertion = insertion;
        this.additionalParams = additionalParams;
    }

    @Override
    protected CallSite beforeInvocation(final Object delegate, final Method method, final Object[] params) {
        return new CallSite(method, delegate, mergeArgumentLists(params));
    }

    private Object[] mergeArgumentLists(final Object[] params) {
        final List<Object> addedParams = new ArrayList<Object>(asList(additionalParams));
        final List<Object> suppliedParams = new ArrayList<Object>(asList(params));
        if (insertion.equals(Insertion.Prefix)) {
            addedParams.addAll(suppliedParams);
            return addedParams.toArray();
        } else {
            suppliedParams.addAll(addedParams);
            return suppliedParams.toArray();
        }
    }
}

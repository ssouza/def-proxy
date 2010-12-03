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

import org.nebularis.defproxy.introspection.MappingException;
import org.nebularis.defproxy.introspection.MethodSignature;

/**
 * Thrown when two methods have incompatible signatures and no
 * {@link org.nebularis.defproxy.introspection.TypeConverter} has been specified.
 */
public class IncompatibleMethodMappingException extends MappingException {

    private final MethodSignature targetSig;

    public IncompatibleMethodMappingException(MethodSignature srcSig, MethodSignature targetSig) {
        super(srcSig);
        this.targetSig = targetSig;
    }

    public MethodSignature getSourceMethodSignature() {
        return sig;
    }

    public MethodSignature getTargetMethodSignature() {
        return targetSig;
    }
}

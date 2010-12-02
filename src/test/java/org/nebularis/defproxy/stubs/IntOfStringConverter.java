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

package org.nebularis.defproxy.stubs;

import org.nebularis.defproxy.introspection.TypeConverter;
import org.nebularis.defproxy.introspection.TypeConverterFactory;

public class IntOfStringConverter implements TypeConverter<String,Integer>, TypeConverterFactory {

    @Override
    public Class<? extends String> getInputType() {
        return String.class;
    }

    @Override
    public Class<? extends Integer> getOutputType() {
        return Integer.class;
    }

    @Override
    public Integer convert(final String o) {
        return Integer.parseInt(String.valueOf(o));
    }

    @Override
    public <T1, T2> TypeConverter<T1, T2> createTypeConverter(final Class<T1> inputClass, final Class<T2> outputClass) {
        if (getInputType().isAssignableFrom(inputClass) && getOutputType().isAssignableFrom(outputClass)) {
            return (TypeConverter<T1, T2>) this;
        }
        return null;
    }
}

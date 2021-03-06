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

/**
 * A factory for type converters. 
 */
public interface TypeConverterFactory {

    /**
     * Create a {@link org.nebularis.defproxy.introspection.TypeConverter} for the specified
     * input and output classes.
     * @param inputClass the class of the input domain
     * @param outputClass the class of the output domain
     * @param <T1> The type of the input class
     * @param <T2> The type of the output class
     * @return a new {@link org.nebularis.defproxy.introspection.TypeConverter} instance, mapping
     * from type <code>T1</code> to <code>T2</code>, or <code>null</code> if no matching converter
     * can be set up for these two types.
     */
    <T1,T2> TypeConverter<T1,T2> createTypeConverter(Class<T1> inputClass, Class<T2> outputClass);

}

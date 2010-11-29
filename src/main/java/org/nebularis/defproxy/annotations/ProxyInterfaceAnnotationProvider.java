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
package org.nebularis.defproxy.annotations;

import java.lang.annotation.*;

/**
 * Provides a means for annotating an annotation
 * to specify that it is a proxy interface provider.
 * In this case, you must provide a delegate accessor
 * that returns a {@link Class}.
 *
 * Example:
 * <pre>
 *      &#064;ProxyInterfaceAnnotationProvider
 *      public @interface MyProxyInterfaceMarker {
 *
 *          Class&lt;?&gt; delegate();
 *
 *          String additionalData default "";
 *      }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ProxyInterfaceAnnotationProvider {}

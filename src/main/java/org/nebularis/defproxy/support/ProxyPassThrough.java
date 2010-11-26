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

/**
 * When this annotates a target interface, all members
 * are passed through to the underlying delegate with
 * a direct mapping of the method signature. Co-variant
 * method return types <i>are</i> supported.
 *
 * Setting {@link org.nebularis.defproxy.support.ProxyPassThrough}
 * at the type level is the equivalent of annotating all the
 * interface's methods with {@link org.nebularis.defproxy.support.ProxyDelegated}.
 */
public @interface ProxyPassThrough {}

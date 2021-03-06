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

/**
* Created by IntelliJ IDEA.
* User: t4
* Date: Nov 26, 2010
* Time: 12:54:53 PM
* To change this template use File | Settings | File Templates.
*/
public class FooBar {

    public void doSomething() {}
    public String getNameFor(final int mapping) {
        return null;
    }
    public Baz returnsSubClass() { return null; }
    public /*check boxed wrappers will work*/ boolean checkCompatibility(final /*check co-variant args */FooBar o) {
        return false;
    }

    public boolean checkCompatibility(final FooBar fb, final String s) {
        return false;
    }

}

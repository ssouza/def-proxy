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

import org.junit.Test;
import org.nebularis.defproxy.stubs.Baz;
import org.nebularis.defproxy.stubs.MyProxyInterface;
import org.nebularis.defproxy.support.MethodSignature;

import java.util.Collection;

public class ProxyConfigurationBuilderTestCase {

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBeAbleToInstantiateWithNullProxyClass() {
        new ProxyConfigurationBuilder(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotBeAbleToInstantiateWithNullDelegateClass() {
        new ProxyConfigurationBuilder(MyProxyInterface.class, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void addingInterfaceMethodMappingShouldThrowForNullValues() throws InvalidInterfaceMappingException {
        final MethodSignature interfaceMethod = null;
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getItemName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, Baz.class);
        builder.delegateMethod(null, delegateMethod);
    }

    @Test(expected=IllegalArgumentException.class)
    public void addingDelegateMethodMappingShouldThrowForNullValues() throws InvalidInterfaceMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = null;
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, Baz.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
    }
    

    /*@Test
    public void byDefaultMethodsAreResolvedBasedOnCompleteSignature() {

        proxyInterface(IF1.class)
            .delegatingTo(Del.class)
            .delegateMethod("name")
                .to("fooey")
            .delegateMethod(String.class, "infinite", String.class)
                to("m_infinite");

        builder.addProxyInterface(MyProxyInterface.class);
        builder.addDelegate(MyDelegate.class);
        //noinspection unchecked
        assertThat((Class)builder.generateHandlerConfiguration().
                getDelegateForProxy(MyProxyInterface.class), is(equalTo((Class)MyDelegate.class)));

    }*/

}


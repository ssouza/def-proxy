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
import org.nebularis.defproxy.stubs.MyDelegate;
import org.nebularis.defproxy.stubs.MyProxyInterface;
import org.nebularis.defproxy.support.MethodSignature;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
    public void addingInterfaceMethodMappingShouldThrowForNullValues() throws InvalidMethodMappingException {
        final MethodSignature interfaceMethod = null;
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getItemName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, Baz.class);
        builder.delegateMethod(null, delegateMethod);
    }

    @Test(expected=IllegalArgumentException.class)
    public void addingDelegateMethodMappingShouldThrowForNullValues() throws InvalidMethodMappingException {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = null;
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, Baz.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
    }
    
    @Test
    public void invalidInterfaceMethodNamesWillThrow() {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getNaame");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateHandlerConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(interfaceMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyProxyInterface.class)));
        }
    }

    @Test
    public void invalidInterfaceReturnTypesWillThrow() {
        final MethodSignature interfaceMethod = new MethodSignature(void.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateHandlerConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(interfaceMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyProxyInterface.class)));
        }
    }

    @Test
    public void invalidInterfaceParameterTypesWillThrow() {
        final MethodSignature interfaceMethod =
                new MethodSignature(void.class, "checkIdentity", String.class, String.class);
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateHandlerConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(interfaceMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyProxyInterface.class)));
        }
    }

     @Test
    public void invalidDelegateMethodNamesWillThrow() {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(String.class, "getFlobby");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateHandlerConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(delegateMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyDelegate.class)));
        }
    }

    @Test
    public void invalidDelegateReturnTypesWillThrow() {
        final MethodSignature interfaceMethod = new MethodSignature(String.class, "getName");
        final MethodSignature delegateMethod = new MethodSignature(void.class, "getName");
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateHandlerConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(delegateMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyDelegate.class)));
        }
    }

    @Test
    public void invalidDelegateParameterTypesWillThrow() {
        final MethodSignature interfaceMethod =
                new MethodSignature(void.class, "checkIdentity", String.class, int.class);
        final MethodSignature delegateMethod =
                new MethodSignature(void.class, "checkIdentity", String.class, String.class);
        final ProxyConfigurationBuilder builder =
                new ProxyConfigurationBuilder(MyProxyInterface.class, MyDelegate.class);
        builder.delegateMethod(interfaceMethod, delegateMethod);
        try {
            builder.generateHandlerConfiguration();
            fail("should not have gotten this far!");
        } catch (InvalidMethodMappingException e) {
            assertThat(e.getInvalidMethodSignature(), is(equalTo(delegateMethod)));
            assertThat((Class) e.getTargetType(), is(equalTo((Class)MyDelegate.class)));
        }
    }

}


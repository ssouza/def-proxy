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
import org.nebularis.defproxy.stubs.FooBar;
import org.nebularis.defproxy.support.MethodInvoker;
import org.nebularis.defproxy.test.AbstractJMockTestSupport;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class HandlerConfigurationTestCase extends AbstractJMockTestSupport {

    @Test
    public void unregisteredSignatureLookupsShouldThrow() throws NoSuchMethodException {
        final Method m = FooBar.class.getMethod("doSomething");
        try {
            new HandlerConfiguration().getMethodInvoker(m);
            fail("should not have arrived here without an exception being thrown");
        } catch (MethodInvocationNotSupportedException e) {
            assertThat(e.getMethod(), is(equalTo(m)));
        }
    }

    @Test
    public void onceRegisteredThenInvokerShouldBeAccessibleFromMethodSignature() throws NoSuchMethodException, MethodInvocationNotSupportedException {
        final Method m = FooBar.class.getMethod("doSomething");
        final MethodInvoker mi = stub(MethodInvoker.class);
        final HandlerConfiguration hc = new HandlerConfiguration();
        hc.registerMethodInvoker(mi, m);

        final MethodInvoker retrieved = hc.getMethodInvoker(m);
        assertThat(retrieved, is(sameInstance(mi)));
    }
}

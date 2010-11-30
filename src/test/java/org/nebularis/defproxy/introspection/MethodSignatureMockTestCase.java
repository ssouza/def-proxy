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

import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nebularis.defproxy.stubs.StoredItem;
import org.nebularis.defproxy.test.AbstractJMockTestSupport;
import org.nebularis.defproxy.utils.TypeConverter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

// and all because you can't run with JMock.class and Theories.class :(
@RunWith(JMock.class)
public class MethodSignatureMockTestCase extends AbstractJMockTestSupport {

    @Test
    public void methodSigUsesTypeConverterWhenSupplied() throws Throwable {
        final TypeConverter converter = mock(TypeConverter.class);
        one(converter).convert(with("59"));
        will(returnValue(59));
        confirmExpectations();

        final StoredItem item = new StoredItem();
        item.set("productId", "59");

        final MethodSignature delegateMethod = new MethodSignature(String.class, "getProductId");
        final MethodInvoker invoker = new MethodInvokerTemplate(delegateMethod);
        invoker.setTypeConverter(converter);

        assertThat((Integer) invoker.handleInvocation(item), is(equalTo(59)));
    }

}

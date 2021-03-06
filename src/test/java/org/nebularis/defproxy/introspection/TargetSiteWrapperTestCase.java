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

import org.junit.Test;
import org.nebularis.defproxy.annotations.Insertion;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TargetSiteWrapperTestCase {

    @Test
    public void templateMethodOverridesCanModifyFormalParameterList() throws Throwable {
        final MethodSignature sig =
                MethodSignature.fromMethod(Map.class.getMethod("get", Object.class));
        final TargetSiteWrapper wrapper = new TargetSiteWrapper(sig, Insertion.Prefix, "foo");

        final Map<String, String> subject = new HashMap<String, String>() {{
            put("foo", "bar");
        }};
        
        final String value = (String) wrapper.handleInvocation(subject);
        assertThat(value, is(equalTo("bar")));
    }

}

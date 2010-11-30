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

package org.nebularis.defproxy.annotations;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.nebularis.defproxy.configuration.ExceptionHandlingPolicy;

import static org.junit.Assert.fail;

@RunWith(Theories.class)
public class ExceptionHandlingPolicyTestCase {

    private static final ExceptionHandlingPolicy fbPolicy = ExceptionHandlingPolicy.wrapExceptions(FooBarException.class);
    public class UselessExceptionType extends RuntimeException {/* no members.... */}

    @DataPoint
    public static Exception ex = new RuntimeException();

    @DataPoint
    public static Exception ex2 = new IllegalArgumentException();

    @DataPoint
    public static Exception ex3 = new UnsupportedOperationException();

    @DataPoint
    public static Exception ex4 = new TypeNotPresentException("", new Exception());

    @Theory
    public void exceptionsAreAlwaysWrappedCorrectly(final Exception ex) throws Throwable {
        try {
            fbPolicy.handleException(ex);
            fail("should not have arrived here....");
        } catch (FooBarException e) {}
    }

}

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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.nebularis.defproxy.introspection.MethodSignature;
import org.nebularis.defproxy.stubs.Baz;
import org.nebularis.defproxy.stubs.FooBar;
import org.nebularis.defproxy.test.ObjectEqualityAndHashCodeVerifier;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.nebularis.defproxy.introspection.ReflectionUtils.isAssignable;

@RunWith(Theories.class)
public class MethodSignatureTestCase extends ObjectEqualityAndHashCodeVerifier<MethodSignature> {

    private static AtomicInteger testCount = new AtomicInteger(0);

    private static final Map<MethodSignature, Object> map = new HashMap<MethodSignature,Object>();

    @Theory
    public void methodSignatureCanBeUsedInHashTable(final MethodSignature sig) {
        assumeThat(sig, is(not(equalTo(null))));
        final Object check = new Object();
        map.put(sig, check);
        assertThat(map.get(sig), is(sameInstance(check)));
    }

    @Theory
    public void identicalMethodSignaturesShouldAlwaysBeCompatible(final MethodSignature m1, final MethodSignature m2) {
        assumeThat(m1, is(equalTo(m2)));
        assertThat(m1, is(compatibleWith(m2)));
    }

    @Theory
    public void differentNamesButCompatibleOtherwiseShouldAlwaysBeTrue(final MethodSignature m1, final MethodSignature m2) {
        assumeThat(m1.getReturnType(), is(assignableFrom(m2.getReturnType())));
        assumeThat(m1.getParameterTypes(), is(assignableFrom(m2.getParameterTypes())));
        // System.out.println("tests run = " + testCount.incrementAndGet());
        assertThat(m1, is(compatibleWith(m2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void conversionFromMethodShouldThrowForNulls() {
        MethodSignature.fromMethod(null);
    }

    @Test
    public void conversionFromMethodShouldPopulateTheCorrectFields() throws NoSuchMethodException {
        final Method m = FooBar.class.getMethod("checkCompatibility", FooBar.class, String.class);
        final MethodSignature sig = MethodSignature.fromMethod(m);
        assertThat(sig.getName(), is(equalTo(m.getName())));
        assertThat(sig.getReturnType(), is(equalTo((Class)m.getReturnType())));
        assertThat(sig.getParameterTypes(), is(equalTo((Class[])m.getParameterTypes())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void supplyingEmptyReturnTypeWillThrow() {
        new MethodSignature(null, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void supplyingEmptyMethodNameWillThrow() {
        new MethodSignature(String.class, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void supplyingNullMethodNameWillThrow() {
        new MethodSignature(String.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void supplyingNullForAnyParameterTypeArrayMemberWillThrow() {
        new MethodSignature(String.class, "method1", String.class, null, Integer.class);
    }

    private Matcher<Class[]> assignableFrom(final Class[] types) {
        return new TypeSafeMatcher<Class[]>() {
            @Override
            public boolean matchesSafely(final Class[] otherTypes) {
                return isAssignable(types, otherTypes);
            }

            @Override
            public void describeTo(final Description description) {}
        };
    }

    private Matcher<Class> assignableFrom(final Class type) {
        return new TypeSafeMatcher<Class>() {
            @Override
            public boolean matchesSafely(final Class aClass) {
                return isAssignable(type, aClass);
            }

            @Override
            public void describeTo(final Description description) {}
        };
    }

    private Matcher<MethodSignature> compatibleWith(final MethodSignature sig) {
        return new TypeSafeMatcher<MethodSignature>() {

            @Override
            public boolean matchesSafely(final MethodSignature methodSignature) {
                return sig.isCompatibleWith(methodSignature);
            }

            @Override
            public void describeTo(final Description description) {}
        };
    }

    @DataPoint
    public static Object nil = null;

    @DataPoint
    public static Object o = new Object();

    @DataPoint
    public static MethodSignature twoParams = new MethodSignature(Class.class, "foobar", String.class, FooBar.class);

    @DataPoint
    public static MethodSignature twoParamsCoVariant2ndArg =
            new MethodSignature(Boolean.class, "foobar", String.class, Baz.class);

    @DataPoint
    public static MethodSignature twoParamsCoVariant2ndArgAndRetType =
            new MethodSignature(boolean.class, "foobar", String.class, Baz.class);

    @DataPoint
    public static MethodSignature twoParamsContraVariant2ndArg =
            new MethodSignature(Class.class, "foobar", String.class, Object.class);

    @DataPoint
    public static MethodSignature twoParams2 = new MethodSignature(Class.class, "foobar2", String.class, FooBar.class);

    @DataPoint
    public static MethodSignature twoParamsCoVariant2ndArg2 =
            new MethodSignature(Boolean.class, "foobar2", String.class, Baz.class);

    @DataPoint
    public static MethodSignature twoParamsCoVariant2ndArgAndRetType2 =
            new MethodSignature(boolean.class, "foobar2", String.class, Baz.class);

    @DataPoint
    public static MethodSignature twoParamsContraVariant2ndArg2 =
            new MethodSignature(Class.class, "foobar2", String.class, Object.class);

    @DataPoint
    public static MethodSignature retClass = new MethodSignature(Class.class, "foobar");

    @DataPoint
    public static MethodSignature retObj = new MethodSignature(Object.class, "foobar");

    @DataPoint
    public static MethodSignature retString = new MethodSignature(String.class, "foobar");

    @DataPoint
    public static MethodSignature fb1RetClass = new MethodSignature(Class.class, "foobar1");

    @DataPoint
    public static MethodSignature fb1RetObj = new MethodSignature(Object.class, "foobar1");

    @DataPoint
    public static MethodSignature fb1RetObjTakeObj = new MethodSignature(Object.class, "foobar1", Object.class);

    @DataPoint
    public static MethodSignature fb1RetObjTakeObjCopy1 = new MethodSignature(Object.class, "foobar1", Object.class);

    @DataPoint
    public static MethodSignature fb1RetObjTakeString = new MethodSignature(FooBar.class, "foobar1", String.class);

    @DataPoint
    public static MethodSignature fb1RetObjTakeStringCopy1 = new MethodSignature(Baz.class, "foobar1", String.class);

    @DataPoint
    public static MethodSignature fb1RetObjTakeString3 = new MethodSignature(Baz.class, "foobar3", Object.class);

    @DataPoint
    public static MethodSignature fb1RetObjTakeString3Copy1 = new MethodSignature(FooBar.class, "foobar3", String.class);

}

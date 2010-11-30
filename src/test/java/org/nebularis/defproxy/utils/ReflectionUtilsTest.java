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

package org.nebularis.defproxy.utils;

import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.nebularis.defproxy.stubs.*;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;
import static org.nebularis.defproxy.utils.ReflectionUtils.isAssignable;
import static org.nebularis.defproxy.utils.ReflectionUtils.primitiveForOrSame;

/**
 * User: carecx
 * Date: 29-Nov-2010
 */
@RunWith(Theories.class)
public class ReflectionUtilsTest {

    @DataPoint
    public static final Class<?> OBJ = Object.class;

    @DataPoint
    public static final Class<?> STR = String.class;

    @DataPoint
    public static final Class<?> NUM = Number.class;

    @DataPoint
    public static final Class<?> CHARSEQ = CharSequence.class;

    @DataPoints
    public static final Class[] PRIMITIVE_CLASSES = {
            Integer.class,
            Long.class,
            Double.class,
            Float.class,
            Character.class,
            Boolean.class,
            Short.class,
            Byte.class
    };

    @DataPoints
    public static final Class[] CUSTOM_TEST_CLASSES = {
            SomeProxyInterface.class,
            Baz.class,
            FooBar.class,
            MyDelegate.class,
            MyProxyInterface.class
    };

    @Test
    public void testSameClassIsAssignable() {
        assertTrue(isAssignable(OBJ, OBJ));
    }

    @Test
    public void testCovarientClasses() {
        assertTrue(isAssignable(OBJ, STR));
        assertFalse(isAssignable(STR, OBJ));
    }

    @Test
    public void testPrimitiveWrappersAreAssignable() {
        assertTrue(isAssignable(Integer.class, Integer.TYPE));
        assertTrue(isAssignable(Integer.TYPE, Integer.class));
        assertTrue(isAssignable(Integer.TYPE, Integer.TYPE));
    }

    @Test
    public void testPrimitiveForOrSame() {                               
        assertSame(Integer.TYPE, primitiveForOrSame(Integer.class));
        assertSame(Integer.TYPE, primitiveForOrSame(Integer.TYPE));
        assertSame(OBJ, primitiveForOrSame(OBJ));
    }

    @Theory
    public void verifyPrimitiveClassesAreAssignableToTheirWrappers(Class clazz) {
        final Class<? extends Object> primitiveClass = primitiveForOrSame(clazz);
        assumeThat(primitiveClass, is(not(sameInstance(clazz))));
        assertTrue(isAssignable(clazz, primitiveClass));
        assertTrue(isAssignable(primitiveClass, clazz));
        assertTrue(isAssignable(primitiveClass, primitiveClass));
    }

    @Theory
    public void classesAreAlwaysAssignableToThemselves(Class clazz) {
        assertTrue(isAssignable(clazz, clazz));
    }

    @Theory
    public void isAssignableBehavesNormallyForNonPrimitives(Class clazzA, Class clazzB) {
        assumeThat(clazzA, is(not(equalTo(clazzB))));
        assumeThat(clazzA.isAssignableFrom(clazzB), is(equalTo(true)));
        assertTrue(isAssignable(clazzA, clazzB));
    }

    @Theory
    public void nullIsNeverAssignable(Class clazz) { 
        assertFalse(isAssignable(clazz, null));
        assertFalse(isAssignable(null, clazz));
        assertFalse(isAssignable(new Class[] { clazz } , (Class[]) null));
        assertFalse(isAssignable((Class[])null, new Class[] { clazz }));
    }


}

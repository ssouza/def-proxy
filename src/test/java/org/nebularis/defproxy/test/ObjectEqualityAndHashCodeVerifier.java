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
package org.nebularis.defproxy.test;

import org.junit.experimental.theories.Theory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public abstract class ObjectEqualityAndHashCodeVerifier<T> {

    // For any non-null reference value x, x.equals(x) should return true
    @Theory
    public void equalsIsReflexive(T x) {
        assumeThat(x, is(not(equalTo(null))));
        assertThat(x.equals(x), is(true));
    }

    // For any non-null reference values x and y, x.equals(y)
    // should return true if and only if y.equals(x) returns true.
    @Theory
    public void equalsIsSymmetric(T x, T y) {
        assumeThat(x, is(not(equalTo(null))));
        assumeThat(y, is(not(equalTo(null))));
        assumeThat(y.equals(x), is(true));
        assertThat(x.equals(y), is(true));
    }

    // For any non-null reference values x, y, and z, if x.equals(y)
    // returns true and y.equals(z) returns true, then x.equals(z)
    // should return true.
    @Theory
    public void equalsIsTransitive(T x, T y, T z) {
        assumeThat(x, is(not(equalTo(null))));
        assumeThat(y, is(not(equalTo(null))));
        assumeThat(z, is(not(equalTo(null))));
        assumeThat(x.equals(y) && y.equals(z), is(true));
        assertThat(z.equals(x), is(true));
    }

    // For any non-null reference values x and y, multiple invocations
    // of x.equals(y) consistently return true  or consistently return
    // false, provided no information used in equals comparisons on
    // the objects is modified.
    @Theory
    public void equalsIsConsistent(T x, T y) {
        assumeThat(x, is(not(equalTo(null))));
        boolean alwaysTheSame = x.equals(y);

        for (int i = 0; i < 30; i++) {
            assertThat(x.equals(y), is(alwaysTheSame));
        }
    }

    // For any non-null reference value x, x.equals(null) should
    // return false.
    @Theory
    public void equalsReturnsFalseOnNull(T x) {
        assumeThat(x, is(not(equalTo(null))));
        assertThat(x.equals(null), is(false));
    }

    // Whenever it is invoked on the same object more than once
    // the hashCode() method must consistently return the same
    // integer.
    @Theory
    public void hashCodeIsSelfConsistent(T x) {
        assumeThat(x, is(not(equalTo(null))));
        int alwaysTheSame = x.hashCode();

        for (int i = 0; i < 30; i++) {
            assertThat(x.hashCode(), is(equalTo(alwaysTheSame)));
        }
    }

    // If two objects are equal according to the equals(Object) method,
    // then calling the hashCode method on each of the two objects
    // must produce the same integer result.
    @Theory
    public void hashCodeIsConsistentWithEquals(T x, T y) {
        assumeThat(x, is(not(equalTo(null))));
        assumeThat(x.equals(y), is(true));
        assertThat(x.hashCode(), is(equalTo(y.hashCode())));
    }

    // Test that x.equals(y) where x and y are the same datapoint
    // instance works. User must provide datapoints that are not equal.
    @Theory
    public void equalsWorks(T x, T y) {
        assumeThat(x, is(not(equalTo(null))));
        assumeThat(x == y, is(true));
        assertThat(x.equals(y), is(true));
    }

}

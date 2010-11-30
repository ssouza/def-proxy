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
package org.nebularis.defproxy.validation;

import org.junit.Test;
import org.nebularis.defproxy.stubs.Baz;
import org.nebularis.defproxy.stubs.FooBar;
import org.nebularis.defproxy.stubs.MyDelegate;
import org.nebularis.defproxy.validation.MethodSignatureValidator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MethodSignatureValidatorTestCase {

    @Test
    public void noMatchingMethodSignatureWillFailValidation() {
        final MethodSignatureValidator validator = new MethodSignatureValidator(MyDelegate.class);
        assertThat(validator.check("getFoo"), is(equalTo(false)));
    }

    @Test
    public void matchingMethodSignatureWillPass() {
        final MethodSignatureValidator validator = new MethodSignatureValidator(FooBar.class);
        assertThat(validator.check("doSomething"), is(equalTo(true)));
    }

    @Test
    public void matchingComplexMethodSignatureWillPass() {
        final MethodSignatureValidator validator = new MethodSignatureValidator(FooBar.class);
        assertThat(validator.check(String.class, "getNameFor", int.class), is(equalTo(true)));
    }

    @Test
    public void invalidReturnTypeInMethodSignatureWillFailValidation() {
        final MethodSignatureValidator validator = new MethodSignatureValidator(FooBar.class);
        assertThat(validator.check(Boolean.class, "getNameFor", int.class), is(equalTo(false)));
    }

    @Test
    public void validReturnTypeInMethodSignatureWillPassValidation() {
        final MethodSignatureValidator validator = new MethodSignatureValidator(FooBar.class);
        assertThat(validator.check(String.class, "getNameFor", int.class), is(equalTo(true)));
    }

    @Test
    public void coVariantReturnTypeInMethodSignatureWillPassValidation() {
        final MethodSignatureValidator validator = new MethodSignatureValidator(FooBar.class);
        assertThat(validator.check(FooBar.class, "returnsSubClass"), is(equalTo(true)));
    }

    @Test
    public void coVariantArgumentTypesInMethodSignatureWillPassValidation() {
        final MethodSignatureValidator validator = new MethodSignatureValidator(FooBar.class);
        assertThat(validator.check(boolean.class, "checkCompatibility", Baz.class), is(equalTo(true)));
    }

    @Test
    public void boxedReturnTypesInMethodSignatureWillPassValidation() {
        final MethodSignatureValidator validator = new MethodSignatureValidator(FooBar.class);
        assertThat(validator.check(Boolean.class, "checkCompatibility", Baz.class), is(equalTo(true)));
    }

    @Test
    public void overloadedMethodSignaturesWillPassValidation() {
        final MethodSignatureValidator validator = new MethodSignatureValidator(FooBar.class);
        assertThat(validator.check(boolean.class, "checkCompatibility", FooBar.class, String.class), is(equalTo(true)));
    }

    @Test
    public void methodWithVoidReturnTypesWillPassValidation() {
        final MethodSignatureValidator validator = new MethodSignatureValidator(FooBar.class);
        assertThat(validator.check(void.class, "doSomething"), is(equalTo(true)));
    }





}

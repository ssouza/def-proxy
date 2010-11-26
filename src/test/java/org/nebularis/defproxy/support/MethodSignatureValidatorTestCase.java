package org.nebularis.defproxy.support;

import org.junit.Test;
import org.nebularis.defproxy.stubs.Baz;
import org.nebularis.defproxy.stubs.FooBar;
import org.nebularis.defproxy.stubs.MyDelegate;

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
}

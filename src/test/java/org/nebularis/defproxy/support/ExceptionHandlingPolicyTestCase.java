package org.nebularis.defproxy.support;

import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

// @RunWith(Theories.class)
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
    
    public class FooBarException extends RuntimeException {
        public FooBarException(final Throwable throwable) {
            super(throwable);
        }
    }

    @Theory
    public void exceptionsAreAlwaysWrappedCorrectly(final Exception ex) throws Throwable {
        try {
            fbPolicy.handleException(ex);
            fail("should not have arrived here....");
        } catch (FooBarException e) {}
    }

}

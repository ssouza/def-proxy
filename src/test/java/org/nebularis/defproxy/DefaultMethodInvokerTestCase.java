package org.nebularis.defproxy;

import org.junit.Test;
import org.nebularis.defproxy.support.MethodSignature;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.nebularis.defproxy.support.ExceptionHandlingPolicy.wrapExceptions;

@SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
public class DefaultMethodInvokerTestCase {

    public class Delegate {
        private String name;

        public Delegate(final String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
        public void setName(final String name) {
            this.name = name;
        }

        public void chuckToysOutOfPram() throws ClassCastException {
            throw new ClassCastException();
        }
    }

    @Test
    public void gettingNameUsingCorrectTypeSignature() throws Throwable {
        final Delegate d = new Delegate("Phil");
        final MethodSignature sig = MethodSignature.fromMethod(d.getClass().getMethod("getName"));
        final DefaultMethodInvoker mi = new DefaultMethodInvoker(sig);

        final Object result = mi.handleInvocation(d, new Object[] {});
        assertThat((String) result, is(equalTo("Phil")));
    }

    @Test(expected = ClassCastException.class)
    public void defaultExceptionHandlingPolicyWillReThrowCauseOfInvocationTargetExceptions() throws Throwable {
        final Delegate d = new Delegate("Phil");
        final Method method = d.getClass().getMethod("chuckToysOutOfPram");

        final DefaultMethodInvoker mi = new DefaultMethodInvoker(MethodSignature.fromMethod(method));
        mi.handleInvocation(d, new Object[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void defaultExceptionHandlingPolicyWillReThrowOtherExceptions() throws Throwable {
        final Delegate d = new Delegate("Phil");

        final DefaultMethodInvoker mi = new DefaultMethodInvoker(null) {
            @Override
            protected Method getMethodBySignature(final Class delegate, final MethodSignature sig) {
                throw new IllegalArgumentException();
            }
        };

        mi.handleInvocation(d, new Object[]{});
    }

    @Test(expected = IllegalArgumentException.class)
    public void suppliedExceptionHandlingPolicyWillOverrideDefault() throws Throwable {
        final Delegate d = new Delegate("Phil");
        final Method method = d.getClass().getMethod("chuckToysOutOfPram");

        final DefaultMethodInvoker mi = new DefaultMethodInvoker(MethodSignature.fromMethod(method));
        mi.setExceptionHandlerPolicy(wrapExceptions(IllegalArgumentException.class));
        mi.handleInvocation(d, new Object[]{});
    }
}

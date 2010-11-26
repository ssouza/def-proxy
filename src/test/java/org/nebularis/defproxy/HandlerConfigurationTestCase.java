package org.nebularis.defproxy;

import org.junit.Test;
import org.nebularis.defproxy.stubs.FooBar;
import org.nebularis.defproxy.support.MethodInvoker;
import org.nebularis.defproxy.test.AbstractJMockTestSupport;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class HandlerConfigurationTestCase extends AbstractJMockTestSupport {

    @Test
    public void unregisteredSignatureLookupsShouldThrow() throws NoSuchMethodException {
        final Method m = FooBar.class.getMethod("doSomething");
        try {
            new HandlerConfiguration().getMethodInvoker(m);
            fail("should not have arrived here without an exception being thrown");
        } catch (MethodInvocationNotSupportedException e) {
            assertThat(e.getMethod(), is(equalTo(m)));
        }
    }

    @Test
    public void onceRegisteredThenInvokerShouldBeAccessibleFromMethodSignature() throws NoSuchMethodException, MethodInvocationNotSupportedException {
        final Method m = FooBar.class.getMethod("doSomething");
        final MethodInvoker mi = stub(MethodInvoker.class);
        final HandlerConfiguration hc = new HandlerConfiguration();
        hc.registerMethodInvoker(mi, m);

        final MethodInvoker retrieved = hc.getMethodInvoker(m);
        assertThat(retrieved, is(sameInstance(mi)));
    }
}

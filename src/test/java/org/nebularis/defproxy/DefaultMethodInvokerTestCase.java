package org.nebularis.defproxy;

import org.junit.Test;
import org.nebularis.defproxy.support.MethodSignature;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
    }

    @Test
    public void gettingNameUsingCorrectTypeSignature() throws NoSuchMethodException {
        final Delegate d = new Delegate("Phil");
        final MethodSignature sig = MethodSignature.fromMethod(d.getClass().getMethod("getName"));
        final DefaultMethodInvoker mi = new DefaultMethodInvoker(sig);

        final Object result = mi.handleInvocation(d, new Object[] {});
        assertThat((String) result, is(equalTo("Phil")));
    }

}
